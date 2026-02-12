package com.saham.hr_system.modules.employees.service;

import com.saham.hr_system.modules.employees.dto.EmployeeDetailsDto;
import com.saham.hr_system.modules.employees.dto.UpdateEmployeeDto;
import org.springframework.web.multipart.MultipartFile;

public interface EmployeeUpdateService {

    /**
     *
     * @return
     */
    EmployeeDetailsDto updateEmployee(Long employeeId, UpdateEmployeeDto updateEmployeeDto);

    void updateEmployeeProfilePicture(Long employeeId, MultipartFile picture);
}
