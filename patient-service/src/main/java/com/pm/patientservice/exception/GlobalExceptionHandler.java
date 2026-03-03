package com.pm.patientservice.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleValidationException(MethodArgumentNotValidException ex){
       Map<String,String> errors= ex.getBindingResult().getFieldErrors().stream().map(
                error->{
                    String fieldName=error.getField();
                    String errorMessage=error.getDefaultMessage();
                    return Map.entry(fieldName,errorMessage);
                }
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
       return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String,String>> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex){

        log.warn("Email already exists: {}", ex.getMessage());
        HashMap<String,String> errorResponse=new HashMap<>();
        errorResponse.put("message",ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
