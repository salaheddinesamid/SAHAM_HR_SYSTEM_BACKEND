package com.saham.hr_system.modules.leave.service;

import com.saham.hr_system.modules.leave.model.LeaveRequest;
import jakarta.mail.MessagingException;

public interface LeaveRequestRejectionEmailSender {

    /**
     * Notify the employee via email when their leave request is rejected by their managers.
     * @param leaveRequest
     */
    void sendSubordinateRejectionEmailToEmployee(LeaveRequest leaveRequest) throws MessagingException;

}
