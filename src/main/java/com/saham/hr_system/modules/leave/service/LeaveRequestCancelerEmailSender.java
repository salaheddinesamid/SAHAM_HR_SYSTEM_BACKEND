package com.saham.hr_system.modules.leave.service;

import com.saham.hr_system.modules.leave.model.LeaveRequest;
import jakarta.mail.MessagingException;

public interface LeaveRequestCancelerEmailSender {

    /**
     * Notify the employee via email when their leave request is approved by their managers.
     * @param leaveRequest
     */
    void notifyEmployee(LeaveRequest leaveRequest) throws MessagingException;

    /**
     * Notify HR via email when a subordinate's leave request is approved by their managers.
     * @param leaveRequest
     */
    void notifyManager(LeaveRequest leaveRequest) throws MessagingException;
}
