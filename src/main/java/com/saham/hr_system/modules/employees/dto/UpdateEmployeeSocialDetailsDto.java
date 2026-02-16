package com.saham.hr_system.modules.employees.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEmployeeSocialDetailsDto {

    private String cnssNumber;
    private String cimrNumber;
    private String insuranceNumber;
}
