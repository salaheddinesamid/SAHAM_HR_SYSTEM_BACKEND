package com.saham.hr_system.modules.analytics.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AbsenceAnalyticsDto {

    private long data;
    private LocalDate from;
    private LocalDate to;
}
