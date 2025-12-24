package com.saham.hr_system.modules.holidays.service;

import com.saham.hr_system.modules.holidays.dto.NewHolidayDto;
import com.saham.hr_system.modules.holidays.model.Holiday;

public interface HolidayAdderService {

    Holiday addHoliday(NewHolidayDto dto);
}
