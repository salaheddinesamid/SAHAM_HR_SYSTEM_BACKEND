package com.saham.hr_system.modules.leave.service;

import com.saham.hr_system.modules.leave.model.LeaveRequest;
import jakarta.mail.MessagingException;

public interface LeaveRequestEmailSender {
    /**
     * This function send emails.
     */
    void sendLeaveApprovalEmail(LeaveRequest leaveRequest) throws MessagingException;

    void sendManagerNotificationEmail(LeaveRequest leaveRequest, String managerEmail) throws MessagingException;
}
