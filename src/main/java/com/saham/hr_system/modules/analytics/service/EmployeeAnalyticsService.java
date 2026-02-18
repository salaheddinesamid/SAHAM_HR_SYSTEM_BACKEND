package com.saham.hr_system.modules.analytics.service;

import com.saham.hr_system.modules.analytics.dto.EmployeeAnalyticsDto;

public interface EmployeeAnalyticsService {
    /**
     * Provides an overview of employee analytics for a specific department and entity.
     * @param department
     * @param entity
     * @return
     */
    EmployeeAnalyticsDto getEmployeeAnalyticsOverview(String department, String entity);
}
