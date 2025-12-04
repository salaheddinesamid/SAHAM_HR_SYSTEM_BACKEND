package com.saham.hr_system.modules.leave.service;

import com.saham.hr_system.modules.leave.model.LeaveRequest;
import jakarta.mail.MessagingException;

public interface LeaveRejectionEmailSender {

    /**
     *
     * @param leaveRequest
     * @throws MessagingException
     */
    void notifyEmployee(LeaveRequest leaveRequest) throws MessagingException;

    /**
     *
     * @param leaveRequest
     * @throws MessagingException
     */
    void notifyManager(LeaveRequest leaveRequest) throws MessagingException;
}
