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

/**
 * Handles the approval workflow for Remote Work absence requests.
 * <p>
 * This includes verifying the manager’s authorization, approving the request at
 * the manager and HR levels, persisting changes, and sending asynchronous notification emails.
 */
@Component
public class RemoteWorkAbsenceRequestApproval implements AbsenceApproval {

    private final EmployeeRepository employeeRepository;
    private final AbsenceRequestRepo absenceRequestRepo;
    private final AbsenceRepository absenceRepository;
    private final AbsenceApprovalEmailSender absenceApprovalEmailSender;
    private final AbsenceRequestApprovalEmailSenderImpl absenceRequestApprovalEmailSender;

    /**
     * Constructs a new RemoteWorkAbsenceRequestApproval instance.
     *
     * @param employeeRepository               repository for employee lookups
     * @param absenceRequestRepo               repository for storing absence requests
     * @param absenceRepository                repository for approved absences
     * @param absenceApprovalEmailSender       sender for final approval emails
     * @param absenceRequestApprovalEmailSender sender for manager-level approval emails
     */
    public RemoteWorkAbsenceRequestApproval(EmployeeRepository employeeRepository,
                                            AbsenceRequestRepo absenceRequestRepo,
                                            AbsenceRepository absenceRepository,
                                            AbsenceApprovalEmailSender absenceApprovalEmailSender,
                                            AbsenceRequestApprovalEmailSenderImpl absenceRequestApprovalEmailSender) {
        this.employeeRepository = employeeRepository;
        this.absenceRequestRepo = absenceRequestRepo;
        this.absenceRepository = absenceRepository;
        this.absenceApprovalEmailSender = absenceApprovalEmailSender;
        this.absenceRequestApprovalEmailSender = absenceRequestApprovalEmailSender;
    }

    /**
     * Checks if this approval handler supports Remote Work absences.
     *
     * @param type the absence type as a string
     * @return true if the type is REMOTE_WORK, false otherwise
     */
    @Override
    public boolean supports(String type) {
        return AbsenceType.REMOTE_WORK.equals(AbsenceType.valueOf(type));
    }

    /**
     * Approves an absence request at the manager (subordinate) level.
     * <p>
     * Performs the following:
     * <ul>
     *     <li>Verifies the manager is authorized</li>
     *     <li>Marks the request as approved by the manager</li>
     *     <li>Saves the change</li>
     *     <li>Sends email notifications asynchronously</li>
     * </ul>
     *
     * @param approvedBy     email of the approving manager
     * @param absenceRequest the request being approved
     */
    @Override
    public void approveSubordinate(String approvedBy, AbsenceRequest absenceRequest) {
        Employee manager = employeeRepository.findByEmail(approvedBy).orElseThrow();

        if (!absenceRequest.getEmployee().getManager().equals(manager)) {
            throw new UnauthorizedAccessException("You are not authorized to approve this absence request.");
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
     * Approves the Remote Work absence request at the HR level.
     * <p>
     * Steps:
     * <ul>
     *     <li>Validates manager approval</li>
     *     <li>Updates the request status to APPROVED</li>
     *     <li>Creates an Absence record</li>
     *     <li>Saves both the request and absence</li>
     *     <li>Sends notifications asynchronously</li>
     * </ul>
     *
     * @param request the absence request to fully approve
     */
    @Override
    public void approve(AbsenceRequest request) {
        if (request.getStatus().equals(AbsenceRequestStatus.APPROVED)) {
            throw new IllegalStateException("Absence request is already approved.");
        }

        if (!request.isApprovedByManager()) {
            throw new IllegalStateException("Absence request is not yet approved by the manager.");
        }

        request.setApprovedByHr(true);
        request.setStatus(AbsenceRequestStatus.APPROVED);

        Absence absence = new Absence();
        absence.setReferenceNumber(request.getReferenceNumber());
        absence.setApprovedAt(LocalDateTime.now());
        absence.setStartDate(request.getStartDate());
        absence.setEndDate(request.getEndDate());
        absence.setTotalDays(request.getTotalDays());
        absence.setEmployee(request.getEmployee());

        absenceRequestRepo.save(request);
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
