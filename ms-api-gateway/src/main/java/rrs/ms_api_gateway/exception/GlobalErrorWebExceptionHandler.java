package rrs.ms_api_gateway.exception;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.webflux.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.cloud.gateway.support.ServiceUnavailableException;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/* import io.github.resilience4j.circuitbreaker.CallNotPermittedException; */
import io.netty.handler.timeout.TimeoutException;
import reactor.core.publisher.Mono;
    

@Component
public class GlobalErrorWebExceptionHandler implements ErrorWebExceptionHandler, Ordered{
    private final ObjectMapper objectMapper;
    private static final Logger log = LoggerFactory.getLogger(GlobalErrorWebExceptionHandler.class);
    

    public GlobalErrorWebExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();

        HttpStatus status;
        String title;
        String detail;
        ProblemDetail gatewayError;


        if (ex instanceof IOException) {
            log.warn("Fallo de red al comunicarse con el servicio: {}", ex.getClass().getSimpleName());
            status = HttpStatus.SERVICE_UNAVAILABLE;
            title = "Service Unavailable";
            detail = "Servicio no encontrado";
        }
        else if (ex instanceof TimeoutException || ex instanceof ResponseStatusException) {
            log.warn("El servicio tardó mucho en responder");
            status = HttpStatus.GATEWAY_TIMEOUT;
            title = "Gateway Timeout";
            detail = "El servidor tardó mucho en responder";
        }
        else if (ex instanceof NotFoundException || ex instanceof ServiceUnavailableException){
            log.warn("No se ha encontrado el servicio solicitado registrado en el server");
            status = HttpStatus.SERVICE_UNAVAILABLE;
            title = "Service Not Found";
            detail = "No se pudo encontrar el servicio";
        }

    /* ##### USAR EN CASO DE TENER FALLBACK CONFIGURADO ##### 
       else if (ex instanceof CallNotPermittedException) {
            log.warn("Circuito abierto");
            status = HttpStatus.SERVICE_UNAVAILABLE;
            title = "Service Unavailable: Open Circuit";
            detail = "El servicio no está disponible temporalmente por fallos repetidos";
        } */
        else {
            log.error("Error inesperado en el gateway: {}", ex.getClass().getName(), ex);
            status = HttpStatus.BAD_GATEWAY;
            title = "Gateway Error";
            detail = "Error en el gateway";
        }
    
        gatewayError = ProblemDetail.forStatusAndDetail(status, detail);
        gatewayError.setTitle(title);


        byte[] jsonBytes;
        try {
            jsonBytes = objectMapper.writeValueAsBytes(gatewayError);

        } catch (JsonProcessingException e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            String fallbackJson = 
            """
            {
                "status":500,
                "title":"Internal Problem",
                "detail":"Ocurrió un error en el servidor"
            }
            """;
            jsonBytes = fallbackJson.getBytes(StandardCharsets.UTF_8);
        }
        
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        DataBuffer buffer = response.bufferFactory().wrap(jsonBytes);
        return response.writeWith(Mono.just(buffer));

    }

    @Override
    public int getOrder() {
        return -2;
    }
}
