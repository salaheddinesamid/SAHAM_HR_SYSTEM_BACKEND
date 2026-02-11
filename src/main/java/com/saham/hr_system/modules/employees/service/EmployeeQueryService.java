package com.saham.hr_system.modules.employees.service;

import com.saham.hr_system.modules.employees.dto.EmployeeDetailsDto;
import com.saham.hr_system.modules.employees.model.Employee;
import org.springframework.data.domain.Page;

import java.util.List;

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
     * @param id
     * @return
     */
    Employee getManager(Long id);

    /**
     *
     * @return
     */
    List<Employee> getAllManagers();

    /**
     *
     * @param email
     * @return
     */
    Employee getEmployeeProfileDetails(String email);
}
