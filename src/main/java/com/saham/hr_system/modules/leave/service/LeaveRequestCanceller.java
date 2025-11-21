package com.saham.hr_system.modules.leave.service;

public interface LeaveRequestCanceller {

    boolean supports(String status);
    void cancel(Long id);
}
