package com.saham.hr_system.modules.employees.service;

import com.saham.hr_system.modules.employees.dto.EmployeeDetailsDto;
import com.saham.hr_system.modules.employees.dto.UpdateEmployeeDto;
import org.springframework.web.multipart.MultipartFile;
/** * Service interface for updating employee information.
 */
public interface EmployeeUpdateService {

    /**
     * Updates the details of an existing employee.
     *
     * @param employeeId The ID of the employee to update.
     * @param updateEmployeeDto The DTO containing the updated employee information.
     * @return The updated employee details.
     */
    EmployeeDetailsDto updateEmployee(Long employeeId, UpdateEmployeeDto updateEmployeeDto);

    /**
     * Updates the profile picture of an existing employee.
     *
     * @param email The email of the employee whose profile picture is to be updated.
     * @param picture The new profile picture file.
     */
    void updateEmployeeProfilePicture(String email, MultipartFile picture);
}
