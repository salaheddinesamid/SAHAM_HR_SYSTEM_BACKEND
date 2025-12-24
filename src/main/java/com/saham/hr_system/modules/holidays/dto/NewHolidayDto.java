package com.saham.hr_system.modules.holidays.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class NewHolidayDto {
    private LocalDate date;
    private String name;
    private int leaveDays;
}
