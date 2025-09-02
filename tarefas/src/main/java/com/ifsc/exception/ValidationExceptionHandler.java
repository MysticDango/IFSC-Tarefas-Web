package com.ifsc.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentValidException(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldError()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

    }
    
}
