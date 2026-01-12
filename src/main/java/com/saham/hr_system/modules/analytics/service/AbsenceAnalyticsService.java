package com.saham.hr_system.modules.analytics.service;

import com.saham.hr_system.modules.analytics.dto.AvgAbsenceDurationDto;
import com.saham.hr_system.modules.analytics.dto.TotalAbsenceDto;
import com.saham.hr_system.modules.analytics.dto.TotalAbsenceRequestDto;

import java.time.LocalDate;

public interface AbsenceAnalyticsService {

    boolean supports(String type);

    /**
     *
     * @return
     */
    TotalAbsenceDto getTotalAbsence();

    /**
     * Retrieve the number of total requests made between two dates with a specific status
     * @param from
     * @param to
     * @param status
     * @return
     */
    TotalAbsenceRequestDto getTotalAbsenceRequests(LocalDate from, LocalDate to, String status);

    /**
     *
     * @return
     */
    AvgAbsenceDurationDto getAvgAbsenceDuration();
}
