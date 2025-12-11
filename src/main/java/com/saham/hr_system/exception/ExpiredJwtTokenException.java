package com.saham.hr_system.exception;

public class ExpiredJwtTokenException extends RuntimeException {
    public ExpiredJwtTokenException(String message) {
        super(message);
    }
}
