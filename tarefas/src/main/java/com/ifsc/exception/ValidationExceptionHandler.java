package com.ifsc.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

// vai ficar escutando as chamadas das apis
@RestControllerAdvice
public class ValidationExceptionHandler {

    // erro 400 - bad request
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        // preparando uma lista vazia para armazenar os erros que vai aparecer
        Map<String, String> errors = new HashMap<>();

        // percorrendo a lista de erros que veio
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            // adiciono na lista o campo que deu erro e sua mensagem 
            errors.put(error.getField(), error.getDefaultMessage());
        }

        ApiError apiError = new ApiError(
            HttpStatus.BAD_REQUEST.value(), 
            "Bad Request", 
            "Erro de validação dos campos",
             request.getDescription(false), 
             errors);

        return ResponseEntity.badRequest().body(apiError);
        // response entity monta os erros
        // ResponseEntity.notFound().build(ApiError);
        // ResponseEntity.ok().build();
        // ResponseEntity.badRequest().build();

    }
    

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleInternalServerError(Exception ex, WebRequest request) {
    
        ApiError apiError = new ApiError(
            HttpStatus.INTERNAL_SERVER_ERROR.value(), 
            "Internal Server Error", 
            "Erro no servidor",
             request.getDescription(false));

        return ResponseEntity.internalServerError().body(apiError);

    }
}
