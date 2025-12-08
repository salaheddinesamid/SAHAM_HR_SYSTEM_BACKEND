package com.saham.hr_system.modules.absence.service.implementation;

import com.saham.hr_system.exception.UnauthorizedAccessException;
import com.saham.hr_system.modules.absence.model.Absence;
import com.saham.hr_system.modules.absence.model.AbsenceRequest;
import com.saham.hr_system.modules.absence.model.AbsenceRequestStatus;
import com.saham.hr_system.modules.absence.model.AbsenceType;
import com.saham.hr_system.modules.absence.repo.AbsenceRepository;
import com.saham.hr_system.modules.absence.repo.AbsenceRequestRepo;
import com.saham.hr_system.modules.absence.service.AbsenceApproval;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

/**
 * This class implement absence approval methods for approving sickness absence requests.
 */
@Component
public class SicknessAbsenceRequestApproval implements AbsenceApproval {
    private final AbsenceRequestRepo absenceRequestRepo;
    private final EmployeeRepository employeeRepository;
    private final AbsenceRepository absenceRepository;
    private final AbsenceRequestApprovalEmailSenderImpl absenceRequestApprovalEmailSender;
    private final AbsenceApprovalEmailSenderImpl absenceApprovalEmailSender;

    @Autowired
    public SicknessAbsenceRequestApproval(AbsenceRequestRepo absenceRequestRepo, EmployeeRepository employeeRepository, AbsenceRepository absenceRepository, AbsenceRequestApprovalEmailSenderImpl absenceRequestApprovalEmailSender, AbsenceApprovalEmailSenderImpl absenceApprovalEmailSender) {
        this.absenceRequestRepo = absenceRequestRepo;
        this.employeeRepository = employeeRepository;
        this.absenceRepository = absenceRepository;
        this.absenceRequestApprovalEmailSender = absenceRequestApprovalEmailSender;
        this.absenceApprovalEmailSender = absenceApprovalEmailSender;
    }

    @Override
    public boolean supports(String type) {
        return AbsenceType.SICKNESS.equals(AbsenceType.valueOf(type));
    }

    @Override
    public void approveSubordinate(String approvedBy, AbsenceRequest absenceRequest) {

        // fetch the manager:
        Employee manager =
                employeeRepository.findByEmail(approvedBy).orElseThrow();

        if(absenceRequest.getStatus().equals(AbsenceRequestStatus.APPROVED)){
            throw new IllegalStateException("This request is already approved.");
        }

        // check if the request is already approved:
        if(absenceRequest.isApprovedByManager()) {
            throw new IllegalStateException("This request is already approved by the manager.");
        }

        // check if the manager is indeed the manager of the employee:
        if(!absenceRequest.getEmployee().getManager().equals(manager)) {
            throw new UnauthorizedAccessException("You are not authorized to approve this request.");
        }
        // Otherwise:
        absenceRequest.setApprovedByManager(true);

        // save the request:
        absenceRequestRepo.save(absenceRequest);

        // notify the employee and HR asynchronously:
        CompletableFuture.runAsync(()->{
            try {
                absenceRequestApprovalEmailSender.notifyEmployee(absenceRequest);
                absenceRequestApprovalEmailSender.notifyHR(absenceRequest);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

    }

    @Override
    public void approve(AbsenceRequest absenceRequest) {
        // check if the request is approved by the manager:
        if(!absenceRequest.isApprovedByManager()) {
            throw new IllegalStateException("This request is not yet approved by the manager.");
        }

        // check if the request is already approved:
        if(absenceRequest.getStatus().equals(AbsenceRequestStatus.APPROVED)){
            throw new IllegalStateException("This request is already approved.");
        }

        // otherwise:
        absenceRequest.setApprovedByHr(true);
        absenceRequest.setStatus(AbsenceRequestStatus.APPROVED);

        // create new absence:
        Absence absence = new Absence();
        absence.setReferenceNumber(absenceRequest.getReferenceNumber());
        absence.setApprovedAt(LocalDateTime.now());
        absence.setStartDate(absenceRequest.getStartDate());
        absence.setEndDate(absenceRequest.getEndDate());
        assert absenceRequest.getEmployee() != null;
        absence.setEmployee(absenceRequest.getEmployee());

        // save the request:
        absenceRequestRepo.save(absenceRequest);
        // save the absence:
        absenceRepository.save(absence);

        // notify the employee and manager asynchronously:
        CompletableFuture.runAsync(()->{
            try {
                absenceApprovalEmailSender.notifyEmployee(absence);
                absenceApprovalEmailSender.notifyManager(absence);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
