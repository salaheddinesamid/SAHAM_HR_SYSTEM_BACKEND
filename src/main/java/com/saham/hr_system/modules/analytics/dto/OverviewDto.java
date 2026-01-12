package com.saham.hr_system.modules.analytics.dto;

import lombok.Data;

@Data
public class OverviewDto {
    TotalAbsenceDto totalAbsences;
    TotalAbsenceRequestDto totalAbsenceRequests;
    TotalLeavesDto totalLeaves;
    TotalLeaveRequestsDto totalLeaveRequests;
    double avgAbsenceDuration;
    double avgLeaveDuration;
}
