package com.saham.hr_system.modules.analytics.service;

import com.saham.hr_system.modules.analytics.dto.ExpenseAnalyticsDto;

import java.util.List;
import java.util.Map;

public interface ExpenseAnalyticsService {
    List<Map<String, Double>> getYearlyOverview(String department, String entity, int year);
}
