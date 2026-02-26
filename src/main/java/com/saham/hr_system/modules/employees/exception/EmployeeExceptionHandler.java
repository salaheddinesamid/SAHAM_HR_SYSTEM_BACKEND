package com.saham.hr_system.modules.employees.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class EmployeeExceptionHandler {

    @ExceptionHandler(EmployeeAlreadyExistsException.class)
    public ResponseEntity<Object> handleEmployeeAlreadyExists(EmployeeAlreadyExistsException ex) {
        return ResponseEntity.status(409).body(
                Map.of("message", ex.getMessage())
        );
    }
}
