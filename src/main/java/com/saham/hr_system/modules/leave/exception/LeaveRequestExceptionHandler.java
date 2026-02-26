package com.saham.hr_system.modules.leave.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class LeaveRequestExceptionHandler {

    @ExceptionHandler(MissingFieldException.class)
    public ResponseEntity<Object> handleMissingFieldException(MissingFieldException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(InvalidDatesException.class)
    public ResponseEntity<Object> handleInvalidDatesException(InvalidDatesException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
