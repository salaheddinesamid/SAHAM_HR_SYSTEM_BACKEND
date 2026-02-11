package com.saham.hr_system.modules.employees.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UpdateEmployeeDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String matriculation;
    private String entity;
    private String occupation;
    private LocalDate joinDate;
    private Long managerId;
    private List<String> roles;
    private EmployeeBalanceDto employeeBalance;
}
