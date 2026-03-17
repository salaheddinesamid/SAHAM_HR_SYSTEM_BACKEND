package com.saham.hr_system.modules.employees.service;


import com.saham.hr_system.modules.employees.dto.EmployeeDetailsDto;
import com.saham.hr_system.modules.employees.dto.NewEmployeeDto;
/** * Service interface for adding new employees to the HR system.
 * This service handles the logic for verifying employee data and adding new employee records.
 */
public interface EmployeeAdderService {

    /**     * Verifies the data for a new employee before adding them to the system.
     * This may include checks for required fields, data formats, and business rules.
     *
     * @return true if the verification is successful, false otherwise
     */
    boolean verify();
    /**     * Adds a new employee to the HR system based on the provided data transfer object.
     * This method assumes that the data has already been verified.
     *
     * @param newEmployeeRequestDto the data transfer object containing the new employee's information
     * @return an EmployeeDetailsDto containing the details of the newly added employee
     */
    EmployeeDetailsDto add(NewEmployeeDto newEmployeeRequestDto);
}
