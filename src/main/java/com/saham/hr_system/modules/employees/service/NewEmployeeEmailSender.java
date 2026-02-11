package com.saham.hr_system.modules.employees.service;

import com.saham.hr_system.modules.employees.dto.EmployeeDetailsDto;
import com.saham.hr_system.modules.employees.model.Employee;

public interface NewEmployeeEmailSender {
    /**
     * Send a welcome email to the new employee with their details and password setup instructions.
     * @param employeeEmail the email address of the new employee
     * @param employeeDetails the details of the new employee
     * @param passwordSetupToken the token for setting up the password
     */
    void sendWelcomeEmail(String employeeEmail, Employee employeeDetails, String passwordSetupToken);
}
