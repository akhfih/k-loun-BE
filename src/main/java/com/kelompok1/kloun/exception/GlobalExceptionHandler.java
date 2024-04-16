package com.kelompok1.kloun.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException e){
        e.printStackTrace();

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("statusCode", e.getStatusCode().value());
        responseBody.put("message", e.getReason());

        return new ResponseEntity<>(responseBody, e.getStatusCode());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(ResponseStatusException e){
        e.printStackTrace();

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("statusCode", e.getStatusCode().value());
        responseBody.put("message", e.getReason());

        return new ResponseEntity<>(responseBody, e.getStatusCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e){
        e.printStackTrace();

        return new ResponseEntity<>("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
