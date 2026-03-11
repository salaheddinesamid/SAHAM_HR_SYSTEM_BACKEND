package com.saham.hr_system.modules.employees.service;

import com.saham.hr_system.modules.employees.dto.EmployeeBalanceResponseDto;
import com.saham.hr_system.modules.employees.dto.EmployeeDetailsDto;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.model.EmployeeBalance;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeeQueryService {
    /**
     * Find all employees with pagination.
     * @param page : the page number to retrieve (0-based index)
     * @param size : the number of records per page
     * @return a Page containing EmployeeDetailsDto objects and pagination information
     */
    Page<EmployeeDetailsDto> getAllEmployees(int page, int size);

    /**
     * Find all employees balances.
     * @return
     */
    List<EmployeeBalanceResponseDto> getAllEmployeesBalances();

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
