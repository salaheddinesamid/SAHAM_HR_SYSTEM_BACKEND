package com.saham.hr_system.modules.analytics.helper;

import com.saham.hr_system.modules.absence.model.AbsenceRequestStatus;
import com.saham.hr_system.modules.absence.model.AbsenceType;
import com.saham.hr_system.modules.absence.repo.AbsenceRepository;
import com.saham.hr_system.modules.absence.repo.AbsenceRequestRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AbsenceAnalyticsHelper {
    private final AbsenceRequestRepo absenceRequestRepo;
    private final AbsenceRepository absenceRepository;

    @Autowired
    public AbsenceAnalyticsHelper(AbsenceRequestRepo absenceRequestRepo, AbsenceRepository absenceRepository) {
        this.absenceRequestRepo = absenceRequestRepo;
        this.absenceRepository = absenceRepository;
    }

    /**
     * Get the total number of requests by filter date (from - to)
     * @param from
     * @param to
     * @return the count of requests between two dates
     */
    public long getCountByFilterDate(LocalDate from, LocalDate to){
        return absenceRequestRepo
                .countByTypeAndIssueDateBetween(
                        AbsenceType.REMOTE_WORK,
                        from.atStartOfDay(), to.atStartOfDay());
    }

    /**
     * Get the total number of requests by filter date and status (REMOTE_WORK, )
     * @param from
     * @param to
     * @param status
     * @return the count of requests between two dates and by status
     */
    public long getCountByFilterDateAndStatus(LocalDate from, LocalDate to, String status){
        return absenceRequestRepo
                .countByTypeAndStatusAndIssueDateBetween(
                        AbsenceType.REMOTE_WORK,
                        AbsenceRequestStatus.valueOf(status),
                        from.atStartOfDay(), to.atStartOfDay());
    }
}
