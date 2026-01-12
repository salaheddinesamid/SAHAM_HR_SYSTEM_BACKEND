package com.saham.hr_system.modules.analytics.dto;

import lombok.Data;

@Data
public class AvgAbsenceDurationDto {
    private double averageDurationInDays;

    public AvgAbsenceDurationDto(double averageDurationInDays) {
        this.averageDurationInDays = averageDurationInDays;
    }
}
