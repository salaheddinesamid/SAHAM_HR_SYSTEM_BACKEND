package com.saham.hr_system.modules.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanAnalyticsDto {

    private long totalLoanRequests;
    private double totalAmountRequested;
    private long totalLoanApproved;
    private long totalLoanRejected;
    private double totalAmountLoanApproved;
    private long totalAmountLoanRejected;
}
