package com.saham.hr_system.modules.employees.service;

import com.saham.hr_system.modules.employees.dto.PasswordUpdateDto;

public interface EmployeePasswordUpdateService {
    /**
     * This method verifies if the old password is correct before allowing the password update.
     * @param oldPassword
     * @param password
     * @return true if the old password is correct and the employee exists, false otherwise.
     */
    boolean verifyOldPassword(String oldPassword, String password);

    /**
     * This method updates the employee's password after verifying the old password.
     * @param email
     * @param passwordUpdateDto
     */
    void updatePassword(String email, PasswordUpdateDto passwordUpdateDto);
}
