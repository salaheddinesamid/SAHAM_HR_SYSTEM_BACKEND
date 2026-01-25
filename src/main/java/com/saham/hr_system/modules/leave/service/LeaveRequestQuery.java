package com.saham.hr_system.modules.leave.service;

import com.saham.hr_system.modules.leave.dto.LeaveRequestResponse;

public interface LeaveRequestQuery {
    /**
     * Get the number of in-process leave requests for subordinates of a manager.
     * @param managerEmail
     * @return
     */
    long getNumberOfInProcessSubordinatesLeaveRequests(String managerEmail);

    /**
     * Get leave request details by reference.
     * @param reference: Ref No of the leave request.
     * @return the leave request details.
     */
    LeaveRequestResponse getLeaveRequestByReference(String reference);
}
