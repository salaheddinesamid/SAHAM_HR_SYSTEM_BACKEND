package com.saham.hr_system.modules.leave.dto;

import com.saham.hr_system.modules.leave.model.LeaveRequest;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class LeaveRequestResponse {

    private Long id;
    private String requestedBy;
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalDays;
    private LocalDateTime requestDate;
    private String type;
    private String document;
    private String status;
    private boolean isApprovedByManager;
    private boolean isApprovedByHr;

    public LeaveRequestResponse(
            LeaveRequest request
    ){
        this.id = request.getLeaveRequestId();
        this.requestedBy = request.getEmployee().getFullName();
        this.startDate = request.getStartDate();
        this.endDate = request.getEndDate();
        this.requestDate = request.getRequestDate();
        this.type = request.getTypeOfLeave().toString();
        this.status = request.getStatus().name();
        this.document = request.getMedicalCertificatePath();
        this.totalDays = request.getTotalDays();
        this.isApprovedByManager = request.isApprovedByManager();
        this.isApprovedByHr = request.isApprovedByHr();
    }

}
