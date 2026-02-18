package com.saham.hr_system.modules.analytics.service;

import com.saham.hr_system.modules.analytics.dto.LeaveAnalyticsDto;

import java.time.LocalDate;

public interface LeaveAnalyticsService {
    /**
     *
     * @return
     */
    LeaveAnalyticsDto getLeaveAnalyticsOverview(String type, LocalDate from, LocalDate to, String department, String entity);
}
