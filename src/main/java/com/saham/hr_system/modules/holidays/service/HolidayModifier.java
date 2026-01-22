package com.saham.hr_system.modules.holidays.service;

import com.saham.hr_system.modules.holidays.dto.HolidayModificationDto;
import com.saham.hr_system.modules.holidays.model.Holiday;

public interface HolidayModifier {
    /**
     * Check if the modifier supports the type of the holiday
     * @param type
     * @return
     */
    boolean supports(String type);
    /**
     *
     * @param dto
     * @param name
     * @return
     */
    Holiday modifyHoliday(String name, HolidayModificationDto dto);
}
