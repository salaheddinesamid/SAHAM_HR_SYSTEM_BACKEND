package com.saham.hr_system.modules.leave.service;

import com.saham.hr_system.modules.leave.model.LeaveRequest;

public interface LeaveRejectionEmailSender {

    /**
     * Notify the employee via email when their leave request is rejected by their managers.
     * @param leaveRequest
     */
    void sendSubordinateApprovalEmailToEmployee(LeaveRequest leaveRequest);

    /**
     * Notify the employee via email when their leave request is rejected by HR.
     * @param leaveRequest
     * @param email
     */
    void sendHRApprovalEmailToEmployee(LeaveRequest leaveRequest, String email);
}
