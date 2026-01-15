package com.saham.hr_system.modules.holidays.dto;

import com.saham.hr_system.modules.holidays.model.Holiday;

/**
 * Event published when a holiday is updated.
 */
public class HolidayUpdatedEvent {

    private final Holiday holiday;

    public HolidayUpdatedEvent(Holiday holiday){
        this.holiday = holiday;
    }
    public Holiday getHoliday() {
        return holiday;
    }
}
