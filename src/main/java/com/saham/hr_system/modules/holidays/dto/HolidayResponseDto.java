package com.saham.hr_system.modules.holidays.dto;

import com.saham.hr_system.modules.holidays.model.Holiday;
import lombok.Data;

import java.util.List;

@Data
public class HolidayResponseDto {

    private List<Holiday> holidays;
    private long totalCount;

    public HolidayResponseDto(List<Holiday> holidays, long totalCount) {
        this.holidays = holidays;
        this.totalCount = totalCount;
    }
}
