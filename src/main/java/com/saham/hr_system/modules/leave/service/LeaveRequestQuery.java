package com.saham.hr_system.modules.leave.service;

public interface LeaveRequestQuery {
    /**
     * Get the number of in-process leave requests for subordinates of a manager.
     * @param managerEmail
     * @return
     */
    long getNumberOfInProcessSubordinatesLeaveRequests(String managerEmail);
}
