package com.saham.hr_system.dto;

import com.saham.hr_system.model.Employee;
import com.saham.hr_system.model.LeaveRequest;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LeaveRequestDetailsDto {

    private EmployeeDetailsDto requestedBy;
    private LocalDate startDate;
    private LocalDate endDate;
    private String type;
    private String status;

    public LeaveRequestDetailsDto(
            Employee employee,
            LeaveRequest request
    ) {
        this.requestedBy = new EmployeeDetailsDto(employee);
        this.startDate = request.getStartDate();
        this.endDate = request.getEndDate();
        this.type = request.getTypeOfLeave();
        this.status = request.getTypeOfLeave();
    }
}

