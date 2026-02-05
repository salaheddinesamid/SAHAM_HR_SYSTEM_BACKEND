package com.saham.hr_system.modules.loan.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class LoanExceptionController {

    @ExceptionHandler(InvalidLoanTypeException.class)
    public ResponseEntity<Object> handleInvalidLoanTypeException(InvalidLoanTypeException ex) {
        return ResponseEntity
                .badRequest()
                .body(
                        Map.of(
                                "message", ex.getMessage()
                        )
                );
    }

    @ExceptionHandler(InvalidCollectionDateException.class)
    public ResponseEntity<Object> handleInvalidCollectionDateException(InvalidCollectionDateException ex) {
        return ResponseEntity
                .badRequest()
                .body(
                        Map.of(
                                "message", ex.getMessage()
                        )
                );
    }

    @ExceptionHandler(InvalidLoanAmountException.class)
    public ResponseEntity<Object> handleInvalidLoanAmountException(InvalidLoanAmountException ex) {
        return ResponseEntity
                .badRequest()
                .body(
                        Map.of(
                                "message", ex.getMessage()
                        )
                );
    }
}
