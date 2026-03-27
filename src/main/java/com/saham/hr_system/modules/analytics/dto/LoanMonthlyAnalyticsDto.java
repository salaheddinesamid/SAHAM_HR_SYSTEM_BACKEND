package com.saham.hr_system.modules.analytics.dto;

import lombok.Data;

@Data
public class LoanMonthlyAnalyticsDto {
    private double totalNormalLoanAmount;
    private double totalInAdvanceLoanAmount;
    private String month;
}
