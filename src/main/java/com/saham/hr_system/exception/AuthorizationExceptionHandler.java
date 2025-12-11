package com.saham.hr_system.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class AuthorizationExceptionHandler {

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<?> handleUnauthorizedAccessException(UnauthorizedAccessException ex) {
        return ResponseEntity
                .status(403)
                .body(
                        Map.of(
                                "message", ex.getMessage()
                        )
                );
    }

    @ExceptionHandler(ExpiredJwtTokenException.class)
    public ResponseEntity<?> handleExpiredJwtTokenException(ExpiredJwtTokenException ex) {
        return ResponseEntity
                .status(401)
                .body(
                        Map.of(
                                "message", ex.getMessage()
                        )
                );
    }
}
