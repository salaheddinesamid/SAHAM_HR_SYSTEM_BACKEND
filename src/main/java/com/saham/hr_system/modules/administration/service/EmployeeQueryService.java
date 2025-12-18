package com.saham.hr_system.modules.administration.service;

import com.saham.hr_system.modules.administration.dto.EmployeeDetailsResponseDto;
import com.saham.hr_system.modules.employees.model.Employee;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface EmployeeQueryService {

    /**
     *
     * @param page
     * @param size
     * @return
     */
    Page<EmployeeDetailsResponseDto> getAllEmployees(int page, int size);

    /**
     *
     * @param fullName
     * @return
     */
    boolean verifyManager(String fullName);
    /**
     *
     * @param fullName
     * @return
     */
    Employee getManager(String fullName);
}
