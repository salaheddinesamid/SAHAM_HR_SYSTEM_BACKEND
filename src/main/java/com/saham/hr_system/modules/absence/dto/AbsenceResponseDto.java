package com.saham.hr_system.modules.absence.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AbsenceResponseDto {

    private Long absenceId;
    private String requestedBy;
    private String absenceType;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime issueDate;
}
