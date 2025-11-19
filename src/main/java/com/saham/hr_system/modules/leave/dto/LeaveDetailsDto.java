package com.saham.hr_system.modules.leave.dto;

import com.saham.hr_system.modules.leave.model.Leave;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LeaveDetailsDto {
    private Long leaveId;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String leaveType;
    private double totalDays;

    public LeaveDetailsDto(Leave leave) {
        this.leaveId = leave.getLeaveId();
        this.fromDate = leave.getFromDate();
        this.toDate = leave.getToDate();
        this.leaveType = leave.getLeaveType().toString();
        this.totalDays = leave.getTotalDays();
    }
}
