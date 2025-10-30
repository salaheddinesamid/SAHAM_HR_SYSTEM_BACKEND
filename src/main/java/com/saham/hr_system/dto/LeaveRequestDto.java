package com.saham.hr_system.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LeaveRequestDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private String type;
    private String comment;
}
