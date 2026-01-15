package com.saham.hr_system.modules.holidays.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class NewHolidayDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean floating;
    private String type;
    private String status;
    private String name;
    private int leaveDays;
}
