package com.saham.hr_system.modules.leave.service;

public interface LeaveRequestCanceler {
    /**
     * Cancel a leave request by its ID.
     * @param requestId
     */
    void cancelLeaveRequest(Long requestId);
}
