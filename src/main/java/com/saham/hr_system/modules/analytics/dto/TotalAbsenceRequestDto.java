package com.saham.hr_system.modules.analytics.dto;

import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

@Data
public class TotalAbsenceRequestDto {
    private long totalAbsenceRequests;
    private LocalDate from;
    private LocalDate to;

    public TotalAbsenceRequestDto(long totalAbsenceRequests, LocalDate from, LocalDate to) {
        this.totalAbsenceRequests = totalAbsenceRequests;
        this.from = from;
        this.to = to;
    }
}
