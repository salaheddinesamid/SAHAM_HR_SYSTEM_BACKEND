package com.saham.hr_system.modules.absence.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AbsenceRequestDto {

    private String type;
    private LocalDate startDate;
    private LocalDate endDate;
}
