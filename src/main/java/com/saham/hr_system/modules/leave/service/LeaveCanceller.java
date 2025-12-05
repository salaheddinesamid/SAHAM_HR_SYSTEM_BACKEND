package com.saham.hr_system.modules.leave.service;

public interface LeaveCanceller {
    boolean supports(String status);
    void cancel(String refNumber);
}
