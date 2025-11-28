package com.saham.hr_system.modules.leave.service;

import com.saham.hr_system.modules.leave.model.LeaveRequest;
import jakarta.mail.MessagingException;

public interface LeaveRequestEmailSender {
    /**
     * This function notify the employee in case of new leave requests made.
     * @param leaveRequest
     * @throws MessagingException
     */
    void sendEmployeeNotificationEmail(LeaveRequest leaveRequest) throws MessagingException;
    /**
     * This function notify the manager in case of new leave request made by their employee.
     * @param leaveRequest
     * @throws MessagingException
     */
    void sendManagerNotificationEmail(LeaveRequest leaveRequest) throws MessagingException;
}
