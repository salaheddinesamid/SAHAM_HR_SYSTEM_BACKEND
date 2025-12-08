package com.saham.hr_system.modules.absence.service.implementation;

import com.saham.hr_system.exception.UnauthorizedAccessException;
import com.saham.hr_system.modules.absence.model.Absence;
import com.saham.hr_system.modules.absence.model.AbsenceRequest;
import com.saham.hr_system.modules.absence.model.AbsenceRequestStatus;
import com.saham.hr_system.modules.absence.model.AbsenceType;
import com.saham.hr_system.modules.absence.repo.AbsenceRepository;
import com.saham.hr_system.modules.absence.repo.AbsenceRequestRepo;
import com.saham.hr_system.modules.absence.service.AbsenceApproval;
import com.saham.hr_system.modules.absence.service.AbsenceApprovalEmailSender;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Component
public class RemoteWorkAbsenceRequestApproval implements AbsenceApproval {

    private final EmployeeRepository employeeRepository;
    private final AbsenceRequestRepo absenceRequestRepo;
    private final AbsenceRepository absenceRepository;
    private final AbsenceApprovalEmailSender absenceApprovalEmailSender;
    private final AbsenceRequestApprovalEmailSenderImpl absenceRequestApprovalEmailSender;

    public RemoteWorkAbsenceRequestApproval(EmployeeRepository employeeRepository, AbsenceRequestRepo absenceRequestRepo, AbsenceRepository absenceRepository, AbsenceApprovalEmailSender absenceApprovalEmailSender, AbsenceRequestApprovalEmailSenderImpl absenceRequestApprovalEmailSender) {
        this.employeeRepository = employeeRepository;
        this.absenceRequestRepo = absenceRequestRepo;
        this.absenceRepository = absenceRepository;
        this.absenceApprovalEmailSender = absenceApprovalEmailSender;
        this.absenceRequestApprovalEmailSender = absenceRequestApprovalEmailSender;
    }

    @Override
    public boolean supports(String type) {
        return AbsenceType.REMOTE_WORK.equals(AbsenceType.valueOf(type));
    }

    @Override
    public void approveSubordinate(String approvedBy, AbsenceRequest absenceRequest) {
        // fetch the manager and check if he/she is the direct manager of the employee
        Employee manager = employeeRepository.findByEmail(approvedBy).orElseThrow();

        if(!absenceRequest.getEmployee().getManager().equals(manager)) {
            throw new UnauthorizedAccessException("You are not authorized to approve this absence request.");
        }
        // otherwise, approve the request
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
    public void approve(AbsenceRequest request) {
        // check if the request is already approved (final approval):
        if(request.getStatus().equals(AbsenceRequestStatus.APPROVED)){
            throw new IllegalStateException("Absence request is already approved.");
        }

        // check if the request is approved by the manager:
        if(!request.isApprovedByManager()){
            throw new IllegalStateException("Absence request is not yet approved by the manager.");
        }
        // otherwise:
        request.setApprovedByHr(true);
        request.setStatus(AbsenceRequestStatus.APPROVED);
        //  create new absence:
        Absence absence = new Absence();
        absence.setReferenceNumber(request.getReferenceNumber());
        absence.setApprovedAt(LocalDateTime.now());
        absence.setStartDate(request.getStartDate());
        absence.setEndDate(request.getEndDate());
        absence.setTotalDays(request.getTotalDays());

        assert request.getEmployee() != null;
        absence.setEmployee(request.getEmployee());
        // save the request:
        absenceRequestRepo.save(request);
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
