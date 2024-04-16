package com.kelompok1.kloun.controller;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.kelompok1.kloun.dto.response.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(JWTCreationException.class)
    public ResponseEntity<CommonResponse<?>> jwtCreationException(JWTCreationException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonResponse.<String []>builder()
                        .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(exception.getMessage())
                        .build());
    }
}
