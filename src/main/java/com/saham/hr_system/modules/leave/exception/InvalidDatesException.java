package com.saham.hr_system.modules.leave.exception;

public class InvalidDatesException extends RuntimeException {
    public InvalidDatesException(String message) {
        super(message);
    }
}
