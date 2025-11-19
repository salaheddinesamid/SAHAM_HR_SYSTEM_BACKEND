package com.saham.hr_system.modules.leave.service.implementation;

import com.saham.hr_system.modules.leave.model.LeaveRequest;
import com.saham.hr_system.modules.leave.service.LeaveRequestEmailSender;
import org.springframework.stereotype.Component;

/**
 * This class implements EmailSender interface to send email.
 */
@Component
public class LeaveApprovalEmailSender implements LeaveRequestEmailSender {
    @Override
    public String generateEmployeeContent(LeaveRequest leaveRequest) {
        return "";
    }

    @Override
    public String generateManagerContent(LeaveRequest leaveRequest) {
        return "";
    }

    @Override
    public void send() {

    }
}
