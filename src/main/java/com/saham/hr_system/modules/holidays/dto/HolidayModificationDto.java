package com.saham.hr_system.modules.holidays.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HolidayModificationDto {
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private int leaveDays;
}
