package com.saham.hr_system.modules.employees.dto;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.List;
@Data
public class NewEmployeeDto {
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String matriculation;
    @NotNull
    private String entity;
    @NotNull
    private String occupation;
    @NotNull
    private String joinDate;
    @NotNull
    private String managerName;
    @NotNull
    private List<String> roles;
    @NotNull
    private EmployeeBalanceDto employeeBalance;
}
