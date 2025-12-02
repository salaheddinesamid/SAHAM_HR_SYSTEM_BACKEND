package com.saham.hr_system.modules.absence.dto;

import com.saham.hr_system.modules.absence.model.AbsenceRequest;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AbsenceRequestDetails {

    private Long absenceRequestId;
    private String referenceNumber;
    private String requestedBy;
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalDays;
    private LocalDateTime issueDate;
    private String type;
    private boolean approvedByManager;
    private boolean approvedByHr;
    private String status;
    private String documentPath;
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
