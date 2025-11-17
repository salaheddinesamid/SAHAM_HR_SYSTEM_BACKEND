package com.saham.hr_system.modules.leave.service;

import com.saham.hr_system.modules.leave.model.Leave;

public interface LeaveApproval {

    boolean supports(String leaveType);
    /**
     *
     * @param requestId
     * @return
     */
    Leave approve(Long requestId);

    /**
     *
     * @param requestId
     */
    void approveSubordinate(Long requestId);

    /**
     *
     * @param requestId
     */
    void rejectSubordinate(Long requestId);

    /**
     *
     * @param requestId
     */
    void rejectLeave(Long requestId);
}
