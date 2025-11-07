package com.saham.hr_system.exception;

public class LeaveRequestAlreadyApprovedException extends RuntimeException {
    public LeaveRequestAlreadyApprovedException(String message) {
        super(message);
    }
}
