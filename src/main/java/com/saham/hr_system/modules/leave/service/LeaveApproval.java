package com.saham.hr_system.modules.leave.service;

import com.saham.hr_system.modules.leave.model.Leave;

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
     * @param requestId
     * @param approvedBy is the email of the manager to check if the manager is the direct manager of the employee
     */
    void approveSubordinate(String approvedBy, Long requestId);

    /**
     *
     * @param requestId
     */
    void rejectSubordinate(String rejectedBy,Long requestId);

    /**
     *
     * @param requestId
     */
    void rejectLeave(Long requestId);
}
