package com.saham.hr_system.modules.holidays.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class HolidayModificationDto {
    private String name;
    private LocalDate date;
    private int leaveDays;
}
