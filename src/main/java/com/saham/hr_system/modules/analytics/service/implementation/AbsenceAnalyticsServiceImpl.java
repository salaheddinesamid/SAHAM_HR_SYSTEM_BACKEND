package com.saham.hr_system.modules.analytics.service.implementation;

import com.saham.hr_system.modules.absence.model.Absence;
import com.saham.hr_system.modules.absence.model.AbsenceRequest;
import com.saham.hr_system.modules.absence.repo.AbsenceRepository;
import com.saham.hr_system.modules.absence.repo.AbsenceRequestRepo;
import com.saham.hr_system.modules.analytics.dto.AbsenceAnalyticsDto;
import com.saham.hr_system.modules.analytics.service.AbsenceAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
        List<Absence> absences = absenceRepository.findAll();
        List<Absence> filteredAbsences = new ArrayList<>();

        List<AbsenceRequest> absenceRequests = absenceRequestRepo.findAll();
        List<AbsenceRequest> filteredAbsenceRequests = new ArrayList<>();

        if(type.equals("ALL")){
            filteredAbsences = absences;
            filteredAbsenceRequests = absenceRequests;
        }

        if(from == null && to == null){
            filteredAbsences = absences;
            filteredAbsenceRequests = absenceRequests;
        }
        if(!type.equals("ALL")){
            filteredAbsences = filterByType(absences,type);
            filteredAbsenceRequests = filterAbsenceRequestByType(absenceRequests,type);
        }
        // If the from and to dates are provided, we filter by the date range
        if(from != null && to != null){
            filteredAbsences = filterAbsencesByDateRange(filteredAbsences, from, to);
            filteredAbsenceRequests = filterAbsenceRequestsByDateRange(filteredAbsenceRequests, from, to);
        }
        // If the department is not ALL, we filter by the specified department
        if(!department.equals("ALL")){
            filteredAbsences = filterAbsencesByDepartment(filteredAbsences, department);
            filteredAbsenceRequests = filterAbsenceRequestsByDepartment(filteredAbsenceRequests, department);
        }
        // If the entity is not ALL, we filter by the specified entity
        if(!entity.equals("ALL")){
            filteredAbsences = filterAbsencesByEntity(filteredAbsences, entity);
            filteredAbsenceRequests = filterAbsenceRequestsByEntity(filteredAbsenceRequests, entity);
        }

        long totalAbsences = filteredAbsences.size();
        long totalApprovedAbsences = filteredAbsenceRequests.stream().filter(request -> request.getStatus().toString().equals("APPROVED")).count();
        long totalPendingAbsences = filteredAbsenceRequests.stream().filter(request -> request.getStatus().toString().equals("IN_PROCESS")).count();
        long totalRejectedAbsences = filteredAbsenceRequests.stream().filter(request -> request.getStatus().toString().equals("REJECTED")).count();
        long totalRequests = absenceRequests.size();

        long totalRemoteWorkLeaveRequests = filteredAbsenceRequests.stream().filter(request -> request.getType().toString().equals("REMOTE_WORK")).count();
        long totalSicknessLeaveRequests = filteredAbsenceRequests.stream().filter(request -> request.getType().toString().equals("SICKNESS")).count();

        double absenceDaysRate = 0;

        return new AbsenceAnalyticsDto(
                totalAbsences,
                totalApprovedAbsences,
                totalRejectedAbsences,
                totalPendingAbsences,
                totalRequests,
                totalRemoteWorkLeaveRequests,
                totalSicknessLeaveRequests,
                absenceDaysRate
        );
    }

    private List<Absence> filterByType(List<Absence> absences, String type) {
        return absences.stream()
                .filter(absence -> absence.getType().toString().equals(type))
                .toList();
    }
    private List<Absence> filterAbsencesByDateRange(List<Absence> leaves, LocalDate from, LocalDate to) {
        return leaves.stream()
                .filter(absence -> (absence.getStartDate().isEqual(from) || absence.getEndDate().isAfter(from)) &&
                        (absence.getEndDate().isEqual(to) || absence.getStartDate().isBefore(to)))
                .toList();
    }

    private List<Absence> filterAbsencesByDepartment(List<Absence> absences, String department) {
        return absences.stream()
                .filter(absence -> absence.getEmployee().getEmployeeProfessionalDetails().getDepartment().toString().equals(department))
                .toList();
    }
    private List<Absence> filterAbsencesByEntity(List<Absence> absences, String entity) {
        return absences.stream()
                .filter(absence -> absence.getEmployee().getEmployeeProfessionalDetails().getEntity().toString().equals(entity))
                .toList();
    }

    // We can also create similar methods for filtering leave requests if needed, for now we are only filtering leaves, but we can easily adapt the same logic to filter leave requests as well.
    private List<AbsenceRequest> filterAbsenceRequestByType(List<AbsenceRequest> requests, String type) {
        return requests.stream()
                .filter(request -> request.getType().toString().equals(type))
                .toList();
    }
    private List<AbsenceRequest> filterAbsenceRequestsByDateRange(List<AbsenceRequest> requests, LocalDate from, LocalDate to) {
        return requests.stream()
                .filter(request -> (request.getStartDate().isEqual(from) || request.getEndDate().isAfter(from)) &&
                        (request.getEndDate().isEqual(to) || request.getStartDate().isBefore(to)))
                .toList();
    }

    private List<AbsenceRequest> filterAbsenceRequestsByDepartment(List<AbsenceRequest> requests, String department) {
        return requests.stream()
                .filter(request -> request.getEmployee().getEmployeeProfessionalDetails().getDepartment().toString().equals(department))
                .toList();
    }
    private List<AbsenceRequest> filterAbsenceRequestsByEntity(List<AbsenceRequest> requests, String entity) {
        return requests.stream()
                .filter(request -> request.getEmployee().getEmployeeProfessionalDetails().getEntity().toString().equals(entity))
                .toList();
    }
}
