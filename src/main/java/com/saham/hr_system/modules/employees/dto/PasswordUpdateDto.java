package com.saham.hr_system.modules.employees.dto;

import lombok.Data;
/** * DTO for updating an employee's password.
 */
@Data
public class PasswordUpdateDto {
    private String oldPassword;
    private String newPassword;
}
