package com.saham.hr_system.modules.employees.dto;

import lombok.Data;

@Data
public class PasswordUpdateDto {
    private String oldPassword;
    private String newPassword;
}
