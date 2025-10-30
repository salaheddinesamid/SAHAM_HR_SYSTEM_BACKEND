package com.saham.hr_system.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class LeaveRequestExceptionController {

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<?> handleInsufficientBalanceException(InsufficientBalanceException ex) {
        return
                ResponseEntity.badRequest().body("Insufficient leave balance: " + ex.getMessage());
    }
}
