package rrs.ms_auth.common.exception;

import java.util.HashMap;
import java.util.Map;


import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import tools.jackson.databind.exc.InvalidFormatException;





@RestControllerAdvice
public class GlobalExceptionHandler {

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
            String errorField = invalidEx.getPath().get(0).getPropertyName();
            apiError = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "El valor enviado para el campo " + errorField + " no es válido");
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
