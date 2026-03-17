package com.saham.hr_system.modules.employees.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.List;
/** * DTO for creating a new employee, including personal, professional, social, and contact details.
 */
@Data
@AllArgsConstructor
public class NewEmployeeDto {
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String sex;
    @NotNull
    private String cin;
    private String address;
    private String nationality;
    @NotNull
    private LocalDate birthDate;
    @NotNull
    private String familyStatus;
    @NotNull
    private Integer numberOfChildren;
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

