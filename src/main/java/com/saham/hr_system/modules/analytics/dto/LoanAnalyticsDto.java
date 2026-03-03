package com.saham.hr_system.modules.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanAnalyticsDto {

    private long totalLoanRequests;
    private long totalLoanApproved;
    private long totalLoanRejected;
    private long totalAmountLoanApproved;
    private long totalAmountLoanRejected;
}
