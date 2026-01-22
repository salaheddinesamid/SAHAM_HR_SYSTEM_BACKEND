package com.saham.hr_system.modules.payroll.dto;

import lombok.Data;

@Data
public class PayrollDetailsDto {
    private int month;
    private String payrollDownloadURL;

    public PayrollDetailsDto(int month, String payrollDownloadURL) {
        this.month = month;
        this.payrollDownloadURL = payrollDownloadURL;
    }
}
