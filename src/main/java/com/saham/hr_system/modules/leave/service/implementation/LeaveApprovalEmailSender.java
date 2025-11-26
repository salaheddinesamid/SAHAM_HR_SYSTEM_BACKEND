package com.saham.hr_system.modules.leave.service.implementation;

import com.saham.hr_system.modules.leave.model.LeaveRequest;
import com.saham.hr_system.modules.leave.service.LeaveRequestEmailSender;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Component;

/**
 * This class implements EmailSender interface to send email.
 */
@Component
public class LeaveApprovalEmailSender implements LeaveRequestEmailSender {

    @Override
    public void sendLeaveApprovalEmail(LeaveRequest leaveRequest) throws MessagingException {

    }

    @Override
    public void sendManagerNotificationEmail(LeaveRequest leaveRequest, String managerEmail) throws MessagingException {

    }
}
