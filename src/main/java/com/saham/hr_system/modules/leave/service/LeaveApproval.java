package com.saham.hr_system.modules.leave.service;

import com.saham.hr_system.modules.leave.model.Leave;
import com.saham.hr_system.modules.leave.model.LeaveRequest;

public interface LeaveApproval {

    boolean supports(String leaveType);
    /**
     *
     * @param requestId
     *
     * @return
     */
    Leave approve(Long requestId);

    /**
     * @param leaveRequest
     * @param approvedBy is the email of the manager to check if the manager is the direct manager of the employee
     */
    void approveSubordinate(String approvedBy, LeaveRequest leaveRequest);

    /**
     *
     * @param leaveRequest
     */
    void rejectSubordinate(String rejectedBy,LeaveRequest leaveRequest);

    /**
     *
     * @param leaveRequest
     */
    void rejectLeave(LeaveRequest leaveRequest);
}
