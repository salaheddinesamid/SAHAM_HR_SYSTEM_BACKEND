package com.saham.hr_system.service;

import com.saham.hr_system.dto.LeaveRequestDto;

public interface LeaveService {

    /**
     * Request leave for an employee.
     * @param leaveRequestDto
     */
    void requestLeave(String email, LeaveRequestDto leaveRequestDto);
}
