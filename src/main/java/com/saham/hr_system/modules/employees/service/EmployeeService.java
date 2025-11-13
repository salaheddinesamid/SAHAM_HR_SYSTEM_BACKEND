package com.saham.hr_system.modules.employees.service;

import com.saham.hr_system.modules.employees.dto.EmployeeDetailsDto;
import com.saham.hr_system.modules.employees.dto.SubordinateDetailsResponseDto;

import java.util.List;

public interface EmployeeService {

    /**
     * Get detailed information about an employee.
     * @param email
     * @return the personal and balance details of the employee.
     */
    EmployeeDetailsDto getEmployeeDetails(String email);

    /**
     * This function fetch all manager subordinates with leaves
     * @param email
     * @return list of employees
     */
    List<SubordinateDetailsResponseDto> getSubordinates(String email);

}
