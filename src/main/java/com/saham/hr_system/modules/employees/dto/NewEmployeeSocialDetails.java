package com.saham.hr_system.modules.employees.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewEmployeeSocialDetails {

    private String cimrNumber;
    private String cnssNumber;
    private String insuranceNumber;
    private String insuranceProvider;
}
