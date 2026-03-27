package com.saham.hr_system.modules.analytics.service;

import com.saham.hr_system.modules.analytics.dto.ExpenseAnalyticsDto;

import java.util.List;
import java.util.Map;

public interface ExpenseAnalyticsService {
    /**
     * Get a monthly overview of expenses for a given department and entity.
     * @param department
     * @param entity
     * @return A map where the key is the month (e.g., "January") and the value is the total expenses for that month.
     */
    List<ExpenseAnalyticsDto> getYearlyOverview(String department, String entity, int year);
}
