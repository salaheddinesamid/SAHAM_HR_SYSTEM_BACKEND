package com.saham.hr_system.modules.analytics.service;

import com.saham.hr_system.modules.analytics.dto.*;

import java.time.LocalDate;

public interface AbsenceAnalyticsService {

    AbsenceAnalyticsDto getAbsenceAnalyticsOverview(String type, LocalDate from, LocalDate to, String department, String entity);

}
