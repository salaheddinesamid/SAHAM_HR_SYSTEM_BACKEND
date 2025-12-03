package com.saham.hr_system.modules.leave.service;

import com.saham.hr_system.modules.leave.model.Leave;
import com.saham.hr_system.modules.leave.model.LeaveRequest;
import jakarta.mail.MessagingException;

public interface LeaveCancelerEmailSender {

    /**
     * Notify the employee via email when their leave request is approved by their managers.
     * @param leave
     */
    void notifyEmployee(Leave leave) throws MessagingException;

    /**
     * Notify HR via email when a subordinate's leave request is approved by their managers.
     * @param leave
     */
    void notifyManager(Leave leave) throws MessagingException;


}
