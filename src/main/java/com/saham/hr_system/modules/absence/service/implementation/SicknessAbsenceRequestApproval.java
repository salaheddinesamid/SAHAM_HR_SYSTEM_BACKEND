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
 * Provides approval workflow logic for Sickness absence requests,
 * including manager approval and final HR approval.
 */
@Component
public class SicknessAbsenceRequestApproval implements AbsenceApproval {

    private final AbsenceRequestRepo absenceRequestRepo;
    private final EmployeeRepository employeeRepository;
    private final AbsenceRepository absenceRepository;
    private final AbsenceRequestApprovalEmailSenderImpl absenceRequestApprovalEmailSender;
    private final AbsenceApprovalEmailSenderImpl absenceApprovalEmailSender;

    /**
     * Constructs a SicknessAbsenceRequestApproval with necessary dependencies.
     *
     * @param absenceRequestRepo               repository for absence requests
     * @param employeeRepository               repository for employee lookups
     * @param absenceRepository                repository for approved absences
     * @param absenceRequestApprovalEmailSender email sender for manager-level approval notifications
     * @param absenceApprovalEmailSender        email sender for HR-level approval notifications
     */
    @Autowired
    public SicknessAbsenceRequestApproval(AbsenceRequestRepo absenceRequestRepo,
                                          EmployeeRepository employeeRepository,
                                          AbsenceRepository absenceRepository,
                                          AbsenceRequestApprovalEmailSenderImpl absenceRequestApprovalEmailSender,
                                          AbsenceApprovalEmailSenderImpl absenceApprovalEmailSender) {
        this.absenceRequestRepo = absenceRequestRepo;
        this.employeeRepository = employeeRepository;
        this.absenceRepository = absenceRepository;
        this.absenceRequestApprovalEmailSender = absenceRequestApprovalEmailSender;
        this.absenceApprovalEmailSender = absenceApprovalEmailSender;
    }

    /**
     * Checks if this handler supports Sickness absence types.
     *
     * @param type the absence type as a string
     * @return true if SICKNESS, false otherwise
     */
    @Override
    public boolean supports(String type) {
        return AbsenceType.SICKNESS.equals(AbsenceType.valueOf(type));
    }

    /**
     * Approves a sickness absence request at the manager level.
     * <p>
     * Responsibilities:
     * <ul>
     *     <li>Validate manager identity</li>
     *     <li>Ensure request is not already approved</li>
     *     <li>Mark as manager-approved</li>
     *     <li>Send email notifications asynchronously</li>
     * </ul>
     *
     * @param approvedBy     manager's email
     * @param absenceRequest the request to approve
     */
    @Override
    public void approveSubordinate(String approvedBy, AbsenceRequest absenceRequest) {
        Employee manager = employeeRepository.findByEmail(approvedBy).orElseThrow();

        if (absenceRequest.getStatus().equals(AbsenceRequestStatus.APPROVED)) {
            throw new IllegalStateException("This request is already approved.");
        }

        if (absenceRequest.isApprovedByManager()) {
            throw new IllegalStateException("This request is already approved by the manager.");
        }

        if (!absenceRequest.getEmployee().getManager().equals(manager)) {
            throw new UnauthorizedAccessException("You are not authorized to approve this request.");
        }

        absenceRequest.setApprovedByManager(true);
        absenceRequestRepo.save(absenceRequest);

        CompletableFuture.runAsync(() -> {
            try {
                absenceRequestApprovalEmailSender.notifyEmployee(absenceRequest);
                absenceRequestApprovalEmailSender.notifyHR(absenceRequest);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Approves a sickness absence request at the HR level.
     * <p>
     * Responsibilities:
     * <ul>
     *     <li>Verify manager approval</li>
     *     <li>Prevent duplicate approvals</li>
     *     <li>Update request status</li>
     *     <li>Create and persist an Absence object</li>
     *     <li>Trigger email notifications</li>
     * </ul>
     *
     * @param absenceRequest the request being fully approved
     */
    @Override
    public void approve(AbsenceRequest absenceRequest) {
        if (!absenceRequest.isApprovedByManager()) {
            throw new IllegalStateException("This request is not yet approved by the manager.");
        }

        if (absenceRequest.getStatus().equals(AbsenceRequestStatus.APPROVED)) {
            throw new IllegalStateException("This request is already approved.");
        }

        absenceRequest.setApprovedByHr(true);
        absenceRequest.setStatus(AbsenceRequestStatus.APPROVED);

        Absence absence = new Absence();
        absence.setReferenceNumber(absenceRequest.getReferenceNumber());
        absence.setApprovedAt(LocalDateTime.now());
        absence.setStartDate(absenceRequest.getStartDate());
        absence.setEndDate(absenceRequest.getEndDate());
        absence.setEmployee(absenceRequest.getEmployee());

        absenceRequestRepo.save(absenceRequest);
        absenceRepository.save(absence);

        CompletableFuture.runAsync(() -> {
            try {
                absenceApprovalEmailSender.notifyEmployee(absence);
                absenceApprovalEmailSender.notifyManager(absence);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
