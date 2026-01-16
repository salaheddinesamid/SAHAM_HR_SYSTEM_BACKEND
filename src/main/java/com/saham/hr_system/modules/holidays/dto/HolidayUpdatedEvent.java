package com.saham.hr_system.modules.holidays.dto;

import com.saham.hr_system.modules.holidays.model.Holiday;

/**
 * Event published when a holiday is updated.
 */
public class HolidayUpdatedEvent {

    private final Holiday holiday;
    private final long totalLeaveDaysUpdated;

    public HolidayUpdatedEvent(Holiday holiday, long totalLeaveDaysUpdated) {
        this.holiday = holiday;
        this.totalLeaveDaysUpdated = totalLeaveDaysUpdated;
    }
    public long getTotalLeaveDaysUpdated(){
        return totalLeaveDaysUpdated;
    }
    public Holiday getHoliday() {
        return holiday;
    }
}
