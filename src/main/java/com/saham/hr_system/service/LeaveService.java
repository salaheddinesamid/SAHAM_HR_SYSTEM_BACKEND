package com.saham.hr_system.service;

import com.saham.hr_system.dto.LeaveRequestDetailsDto;
import com.saham.hr_system.dto.LeaveRequestDto;
import com.saham.hr_system.dto.LeaveRequestResponse;
import com.saham.hr_system.model.LeaveRequest;

import java.util.List;

public interface LeaveService {

    /**
     * Request leave for an employee.
     * @param leaveRequestDto
     */
    void requestLeave(String email, LeaveRequestDto leaveRequestDto);

    /**
     * Get all leave requests made by an employee.
     * @param email
     * @return list of leave requests.
     */
    List<LeaveRequestResponse> getAllLeaveRequests(String email);
}
