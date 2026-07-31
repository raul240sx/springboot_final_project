package rrs.ms_sales.common.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import feign.FeignException;



@RestControllerAdvice
public class GlobalExceptionHandler {
    private final ObjectMapper objectMapper;
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public GlobalExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    


    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthentication(AuthenticationException ex){
        ProblemDetail apiError = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
        apiError.setTitle("Unauthorized");
        return apiError;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDenied(AccessDeniedException ex) {
        ProblemDetail apiError = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getMessage());
        apiError.setTitle("Access Denied");
        return apiError;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFound(ResourceNotFoundException ex) {
        ProblemDetail apiError = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        apiError.setTitle("Resource Not Found");
        return apiError;
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ProblemDetail handleBusinessRule(BusinessRuleException ex) {
        ProblemDetail apiError = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        apiError.setTitle("Business Rule Error");
        return apiError;
    }
    
    
    @ExceptionHandler(SaleProcessingException.class)
    public ProblemDetail handleSaleProcessing(SaleProcessingException ex) {
        log.error("Error procesando venta", ex);
        ProblemDetail apiError = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        apiError.setTitle("Sale Processing Error");
        return apiError;
    }

    @ExceptionHandler(FeignException.class)
    public ProblemDetail handleFeign(FeignException ex) {
        HttpStatus status = HttpStatus.resolve(ex.status());
        ProblemDetail apiError;
        if (status != null) {
            String responseBody = ex.contentUTF8();

            try {
                JsonNode originalError = objectMapper.readTree(responseBody);
                String originalMessage = originalError.path("detail").asText();
                String originalTitle = originalError.path("title").asText();
                apiError = ProblemDetail.forStatusAndDetail(status, originalMessage.isBlank() ? status.getReasonPhrase() : originalMessage);
                apiError.setTitle(originalTitle);
                return apiError;

            } catch (Exception e) {
                apiError = ProblemDetail.forStatusAndDetail(status, "No se pudo procesar la respuesta");
                apiError.setTitle(status.getReasonPhrase());
                return apiError;
            }
        }
        apiError = ProblemDetail.forStatusAndDetail(HttpStatus.SERVICE_UNAVAILABLE, "Error en la comunicación entre servicios");
        apiError.setTitle("Service Communication Error");
        return apiError;
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errorMesages = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errorMesages.put(error.getField(), error.getDefaultMessage()));

        ProblemDetail apiError = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "La solicitud contiene campos inválidos");
        apiError.setProperty("invalidFields", errorMesages);
        apiError.setTitle("Validation Error");
        return apiError;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleUnreadableMessage(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();

        ProblemDetail apiError;

        if (cause instanceof InvalidFormatException) {
            InvalidFormatException invalidEx = (InvalidFormatException) cause;
            if (!invalidEx.getPath().isEmpty()) {
                String errorField = invalidEx.getPath().get(0).getFieldName();
                apiError = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "El valor enviado para el campo " + errorField + " no es válido");
            }
            else {
                apiError = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "El tipo de dato enviado no corresponde al esperado");
            }
        }
        else {
            apiError = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Mensaje JSON mal formateado");
        }
        apiError.setTitle("Malformed Request Json");

        return apiError;
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ProblemDetail handleNoResourceFound(NoResourceFoundException ex) {
        ProblemDetail apiError = ProblemDetail.forStatusAndDetail(ex.getStatusCode(), "El recurso solicitado no existe");
        apiError.setTitle("Resource Not found");
        return apiError;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleServerError(Exception ex) {
        ProblemDetail apiError = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado en el servidor, intente más tarde");
        apiError.setTitle("Internal Error");
        return apiError;
    }

}
