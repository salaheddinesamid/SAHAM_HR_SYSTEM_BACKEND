package com.saham.hr_system.modules.leave.service;

import com.saham.hr_system.modules.leave.model.LeaveRequest;

public interface LeaveRequestEmailSender {

    String generateEmployeeContent(LeaveRequest leaveRequest);
    String generateManagerContent(LeaveRequest leaveRequest);

    /**
     * This function send emails.
     */
    void send();
}
