package com.saham.hr_system.modules.analytics.service;

import com.saham.hr_system.modules.analytics.dto.LoanAnalyticsDto;

import java.time.LocalDate;

public interface LoanAnalyticsService {

    LoanAnalyticsDto getLoanAnalyticsOverview(String type, LocalDate from, LocalDate to, String department, String entity);
}
