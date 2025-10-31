package com.saham.hr_system.service;

import com.saham.hr_system.dto.EmployeeDetailsDto;

public interface EmployeeService {

    /**
     * Get detailed information about an employee.
     * @param email
     * @return the personal and balance details of the employee.
     */
    EmployeeDetailsDto getEmployeeDetails(String email);


}
