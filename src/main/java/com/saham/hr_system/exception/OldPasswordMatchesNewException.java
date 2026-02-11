package com.saham.hr_system.exception;

public class OldPasswordMatchesNewException extends RuntimeException {
    public OldPasswordMatchesNewException(String message) {
        super(message);
    }
}
