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
    private String CIN;
    @NotNull
    private String familyStatus;
    @NotNull
    private Integer numberOfChildren;
    @NotNull
    private String email;

    @NotNull
    private NewEmployeeProfessionalDetailsDto professionalDetailsDto; // professional details
    @NotNull
    private NewEmployeeSocialDetails employeeSocialDetailsDto; // social details
    @NotNull
    private NewEmployeeContactDetails employeeContactDetailsDto; // contact details
    @NotNull
    private List<String> roles;
    @NotNull
    private EmployeeBalanceDto employeeBalance;
}

