package com.saham.hr_system.modules.absence.dto;

import com.saham.hr_system.modules.absence.model.AbsenceRequest;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO representing the full details of an absence request.
 * <p>
 * This class is used to expose processed absence information to the client,
 * including request metadata, approver status, and attached medical documents.
 * </p>
 */
@Data
public class AbsenceRequestDetails {

    /** Unique identifier of the absence request. */
    private Long absenceRequestId;

    /** Unique reference number assigned to the request. */
    private String referenceNumber;

    /** Full name of the employee who submitted the request. */
    private String requestedBy;

    /** Requested start date of the absence period. */
    private LocalDate startDate;

    /** Requested end date of the absence period. */
    private LocalDate endDate;

    /** Total number of absence days calculated by the system. */
    private double totalDays;

    /** Date and time when the request was issued. */
    private LocalDateTime issueDate;

    /** Type of absence (e.g., SICKNESS, REMOTE_WORK, VACATION). */
    private String type;

    /** Indicates if the request has been approved by the employee's manager. */
    private boolean approvedByManager;

    /** Indicates if the request has been approved by HR. */
    private boolean approvedByHr;

    /** Current status of the request (e.g., PENDING, APPROVED, REJECTED). */
    private String status;

    /** File path of the uploaded medical certificate, if provided. */
    private String documentPath;

    /**
     * Constructs the DTO by mapping data from the {@link AbsenceRequest} entity.
     *
     * @param absenceRequest the domain model entity to map from
     */
    public AbsenceRequestDetails(AbsenceRequest absenceRequest) {
        this.absenceRequestId = absenceRequest.getAbsenceRequestId();
        this.referenceNumber = absenceRequest.getReferenceNumber();
        this.requestedBy = absenceRequest.getEmployee().getFullName();
        this.startDate = absenceRequest.getStartDate();
        this.endDate = absenceRequest.getEndDate();
        this.issueDate = absenceRequest.getIssueDate();
        this.totalDays = absenceRequest.getTotalDays();
        this.type = absenceRequest.getType().toString();
        this.approvedByManager = absenceRequest.isApprovedByManager();
        this.approvedByHr = absenceRequest.isApprovedByHr();
        this.status = absenceRequest.getStatus().toString();
        this.documentPath = absenceRequest.getMedicalCertificatePath();
    }
}
