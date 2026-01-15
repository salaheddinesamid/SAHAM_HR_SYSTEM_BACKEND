package com.saham.hr_system.modules.holidays.service;

import com.saham.hr_system.modules.holidays.dto.HolidayResponseDto;
import com.saham.hr_system.modules.holidays.model.Holiday;

import java.util.List;

public interface HolidayQueryService {
    /**
     *
     * @return
     */
    HolidayResponseDto getAllHolidays();
}
