package com.saham.hr_system.modules.leave.service;

import com.saham.hr_system.modules.leave.model.LeaveRequest;
import jakarta.mail.MessagingException;

public interface LeaveRequestApprovalEmailSender {

    /**
     * Notify the employee via email when their leave request is approved by their managers.
     * @param leaveRequest
     * @param email
     */
    void sendSubordinateApprovalEmailToEmployee(LeaveRequest leaveRequest, String email) throws MessagingException;

    /**
     * Notify HR via email when a subordinate's leave request is approved by their managers.
     * @param leaveRequest
     */
    void sendSubordinateApprovalEmailToHR(LeaveRequest leaveRequest);

    /**
     * Notify the employee via email when their leave request is approved by HR.
     * @param leaveRequest
     */
    void sendHRApprovalEmailToEmployee(LeaveRequest leaveRequest, String email);
}
