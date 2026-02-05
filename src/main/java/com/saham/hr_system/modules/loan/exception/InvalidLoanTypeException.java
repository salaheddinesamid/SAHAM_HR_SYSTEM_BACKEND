package com.saham.hr_system.modules.loan.exception;

public class InvalidLoanTypeException extends RuntimeException {
    public InvalidLoanTypeException(String message) {
        super(message);
    }
}
