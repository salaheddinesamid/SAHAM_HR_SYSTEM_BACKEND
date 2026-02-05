package com.saham.hr_system.modules.loan.exception;

public class InvalidLoanAmountException extends RuntimeException {
    public InvalidLoanAmountException(String message) {
        super(message);
    }
}
