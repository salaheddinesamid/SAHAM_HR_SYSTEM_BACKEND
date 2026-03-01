package com.saham.hr_system.modules.analytics.service.implementation;

import com.saham.hr_system.modules.absence.repo.AbsenceRepository;
import com.saham.hr_system.modules.absence.repo.AbsenceRequestRepo;
import com.saham.hr_system.modules.analytics.dto.AbsenceAnalyticsDto;
import com.saham.hr_system.modules.analytics.service.AbsenceAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AbsenceAnalyticsServiceImpl implements AbsenceAnalyticsService {

    private final AbsenceRepository absenceRepository;
    private final AbsenceRequestRepo absenceRequestRepo;

    @Autowired
    public AbsenceAnalyticsServiceImpl(AbsenceRepository absenceRepository, AbsenceRequestRepo absenceRequestRepo) {
        this.absenceRepository = absenceRepository;
        this.absenceRequestRepo = absenceRequestRepo;
    }

    @Override
    public AbsenceAnalyticsDto getAbsenceAnalyticsOverview(String type, LocalDate from, LocalDate to, String department, String entity) {
        return null;
    }
}
