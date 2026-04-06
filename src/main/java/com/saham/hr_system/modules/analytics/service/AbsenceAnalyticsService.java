package com.saham.hr_system.modules.analytics.service;

import com.saham.hr_system.modules.analytics.dto.*;

import java.time.LocalDate;
public interface AbsenceAnalyticsService {
    /**
     * Get the absence analytics overview for a given type, date range, department and entity
     * @param type the type of the analytics (e.g. "absence", "leave", etc.)
     * @param from the start date of the date range
     * @param to the end date of the date range
     * @param department the department to filter by (optional)
     * @param entity the entity to filter by (optional)
     * @return an AbsenceAnalyticsDto containing the analytics overview
     */
    AbsenceAnalyticsDto getAbsenceAnalyticsOverview(String type, LocalDate from, LocalDate to, String department, String entity);

}
