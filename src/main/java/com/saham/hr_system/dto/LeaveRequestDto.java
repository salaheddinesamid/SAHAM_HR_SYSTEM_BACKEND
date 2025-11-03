package com.saham.hr_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveRequestDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private String type;
    private String comment;
}
