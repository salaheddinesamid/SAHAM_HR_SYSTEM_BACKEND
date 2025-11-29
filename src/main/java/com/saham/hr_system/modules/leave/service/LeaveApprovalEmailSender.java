package com.saham.hr_system.modules.leave.service;

import com.saham.hr_system.modules.leave.model.LeaveRequest;
import jakarta.mail.MessagingException;

public interface LeaveApprovalEmailSender {

    /**
     * Notify the manager via email when their subordinate's leave request is approved by HR.
     * @param leaveRequest
     */
    void sendHRApprovalEmailToManager(LeaveRequest leaveRequest) throws MessagingException;

    /**
     * Notify the employee via email when their leave request is approved by HR.
     * @param leaveRequest
     */
    void sendHRApprovalEmailToEmployee(LeaveRequest leaveRequest) throws MessagingException;
}
