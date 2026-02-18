package com.saham.hr_system.modules.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveAnalyticsDto {
    private long totalLeaves;
    private long totalApprovedLeaves;
    private long totalRejectedLeaves;
    private long totalRequests;
    private long leaveDaysRate;
}
