package com.saham.hr_system.modules.analytics.service.implementation;

import com.saham.hr_system.modules.absence.model.Absence;
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
public class RemoteWorkAbsenceAnalytics implements AbsenceAnalyticsService {

    private final AbsenceRepository absenceRepository;
    private final AbsenceRequestRepo absenceRequestRepo;
    private final AbsenceAnalyticsHelper absenceAnalyticsHelper;
    @Autowired
    public RemoteWorkAbsenceAnalytics(AbsenceRepository absenceRepository, AbsenceRequestRepo absenceRequestRepo, AbsenceAnalyticsHelper absenceAnalyticsHelper) {
        this.absenceRepository = absenceRepository;
        this.absenceRequestRepo = absenceRequestRepo;
        this.absenceAnalyticsHelper = absenceAnalyticsHelper;
    }

    @Override
    public boolean supports(String type) {
        return AbsenceType.REMOTE_WORK.equals(AbsenceType.valueOf(type));
    }

    @Override
    public TotalAbsenceDto getTotalAbsence() {
        // fetch the total absence from db
        long totalAbsence = absenceRepository.count();
        return new TotalAbsenceDto(totalAbsence);
    }

    @Override
    public TotalAbsenceRequestDto getTotalAbsenceRequests(LocalDate from, LocalDate to, String status) {

        long totalRequests = 0;
        // If filter status is set to ALL by default
        if(status.equals("ALL")){
            // When the filter by date is given
            if(from != null && to != null){
                totalRequests = absenceAnalyticsHelper.getCountByFilterDate(from, to);
            }

            // In a normal case
            if(from == null && to == null){
                totalRequests = absenceRequestRepo
                        .countByType(AbsenceType.REMOTE_WORK);
            }
        }
        // If filter by status is applied
        if(!status.equals("ALL")){
            // When the filter by date is given
            if(from != null && to != null){
                totalRequests = absenceAnalyticsHelper.getCountByFilterDate(from, to);
            }

            // In a normal case
            if(from == null && to == null){
                totalRequests = absenceRequestRepo
                        .countByType(AbsenceType.REMOTE_WORK);
            }
        }else{
            if(from != null && to != null){
                totalRequests = absenceAnalyticsHelper.getCountByFilterDateAndStatus(from, to, status);
            }
        }

        return new TotalAbsenceRequestDto(
                totalRequests,
                from,
                to
        );
    }



    @Override
    public AvgAbsenceDurationDto getAvgAbsenceDuration() {
        // Get the total number of leaves:
        long totalAbsences = absenceRepository.count();
        // Calculate the sum of total absence duration:
        double totalDuration = absenceRepository
                .findAll()
                .stream()
                .mapToDouble(Absence::getTotalDays)
                .sum();

        return new AvgAbsenceDurationDto(
                totalDuration / totalAbsences
        );
    }
}
