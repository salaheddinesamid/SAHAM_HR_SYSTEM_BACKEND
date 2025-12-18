package com.saham.hr_system.modules.administration.service;

import com.saham.hr_system.modules.administration.dto.EmployeeDetailsResponseDto;
import com.saham.hr_system.modules.administration.dto.NewEmployeeRequestDto;
import com.saham.hr_system.modules.employees.dto.EmployeeDetailsDto;

public interface EmployeeManagementService {

    /**
     *
     * @param requestDto
     * @return
     */
    EmployeeDetailsResponseDto newEmployee(NewEmployeeRequestDto requestDto);

    /**
     *
     * @param matriculation
     * @return
     */
    EmployeeDetailsDto modifyEmployee(String matriculation);

    /**
     *
     * @param matriculation
     */
    void offBoardEmployee(String matriculation);
}
