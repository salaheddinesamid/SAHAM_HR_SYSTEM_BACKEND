package com.saham.hr_system.modules.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbsenceAnalyticsDto {

    private long totalAbsences;
    private long totalApprovedAbsences;
    private long totalRejectedAbsences;
    private long totalPendingAbsenceRequests;
    private long totalAbsenceRequests;
    private double absenceDaysRate;
}
