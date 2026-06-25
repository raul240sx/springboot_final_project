package com.store.sales_api.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import tools.jackson.databind.exc.InvalidFormatException;





@RestControllerAdvice
public class GlobalExceptionHandler {

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

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleServerError(Exception ex) {
        ProblemDetail apiError = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado en el servidor, intente más tarde");
        apiError.setTitle("Internal Error");
        return apiError;
    }

}
