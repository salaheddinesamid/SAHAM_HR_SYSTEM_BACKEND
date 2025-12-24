package com.saham.hr_system.modules.employees.service;

import com.saham.hr_system.modules.employees.dto.EmployeeDetailsDto;
import com.saham.hr_system.modules.employees.model.Employee;
import org.springframework.data.domain.Page;

public interface EmployeeQueryService {
    /**
     *
     * @param page
     * @param size
     * @return
     */
    Page<EmployeeDetailsDto> getAllEmployees(int page, int size);

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
