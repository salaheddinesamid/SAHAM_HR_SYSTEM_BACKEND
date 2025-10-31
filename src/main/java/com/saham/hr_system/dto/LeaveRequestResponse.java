package com.saham.hr_system.dto;

import com.saham.hr_system.model.LeaveRequest;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class LeaveRequestResponse {

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime requestDate;
    private String type;
    private String status;

    public LeaveRequestResponse(
            LeaveRequest request
    ){
        this.startDate = request.getStartDate();
        this.endDate = request.getEndDate();
        this.requestDate = request.getRequestDate();
        this.type = request.getTypeOfLeave();
        this.status = request.getStatus().name();
    }

}
