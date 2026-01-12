package com.saham.hr_system.modules.analytics.service.implementation;

import com.saham.hr_system.modules.absence.model.AbsenceType;
import com.saham.hr_system.modules.absence.repo.AbsenceRepository;
import com.saham.hr_system.modules.absence.repo.AbsenceRequestRepo;
import com.saham.hr_system.modules.analytics.dto.AvgAbsenceDurationDto;
import com.saham.hr_system.modules.analytics.dto.TotalAbsenceDto;
import com.saham.hr_system.modules.analytics.dto.TotalAbsenceRequestDto;
import com.saham.hr_system.modules.analytics.helper.AbsenceAnalyticsHelper;
import com.saham.hr_system.modules.analytics.service.AbsenceAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SicknessAbsenceAnalytics implements AbsenceAnalyticsService {

    private final AbsenceRequestRepo absenceRequestRepo;
    private final AbsenceRepository absenceRepository;
    private final AbsenceAnalyticsHelper absenceAnalyticsHelper;

    @Autowired
    public SicknessAbsenceAnalytics(AbsenceRequestRepo absenceRequestRepo, AbsenceRepository absenceRepository, AbsenceAnalyticsHelper absenceAnalyticsHelper) {
        this.absenceRequestRepo = absenceRequestRepo;
        this.absenceRepository = absenceRepository;
        this.absenceAnalyticsHelper = absenceAnalyticsHelper;
    }

    @Override
    public boolean supports(String type) {
        return AbsenceType.SICKNESS.equals(AbsenceType.valueOf(type));
    }

    @Override
    public TotalAbsenceDto getTotalAbsence() {
        return null;
    }

    @Override
    public TotalAbsenceRequestDto getTotalAbsenceRequests(LocalDate from, LocalDate to, String status) {
        long totalAbsenceRequests = 0;
        if (status.equals("ALL")) {
            if (from != null && to != null) {
                totalAbsenceRequests = absenceAnalyticsHelper.getCountByFilterDate(
                        from, to
                );
            } else {
                totalAbsenceRequests = absenceRequestRepo.countByType(
                        AbsenceType.SICKNESS
                );
            }
        } else {
            if (from != null && to != null) {
                totalAbsenceRequests = absenceAnalyticsHelper.getCountByFilterDateAndStatus(
                        from, to, status
                );
            }
        }
        return new TotalAbsenceRequestDto(
                totalAbsenceRequests,
                from,
                to
        );
    }
    @Override
    public AvgAbsenceDurationDto getAvgAbsenceDuration () {
        // get the total duration of all sickness absences
        double avgAbsenceDuration = absenceRepository.getAbsenceAVG();
        return new AvgAbsenceDurationDto(
                avgAbsenceDuration
        );
    }
}
