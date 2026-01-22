package com.saham.hr_system.modules.payroll.dto;

import lombok.Data;

import java.util.List;

@Data
public class EmployeePayrollDetailsDto {
    int year;
    List<PayrollDetailsDto> details;

    public EmployeePayrollDetailsDto(int year, List<PayrollDetailsDto> details) {
        this.year = year;
        this.details = details;
    }
}
