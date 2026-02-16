package com.saham.hr_system.modules.employees.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEmployeeDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<String> roles;
    private EmployeeBalanceDto employeeBalance;
    private UpdateEmployeeProDetailsDto professionalDetailsDto;
    private UpdateEmployeeSocialDetailsDto socialDetailsDto;
    private UpdateEmployeeContactDetailsDto contactDetailsDto;
}
