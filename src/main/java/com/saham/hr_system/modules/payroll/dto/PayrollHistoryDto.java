package com.saham.hr_system.modules.payroll.dto;

import com.saham.hr_system.modules.payroll.model.PayrollHistory;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PayrollHistoryDto {
    private long payrollHistoryId;
    private LocalDateTime executionDate;
    private int numberOfEmployees;
    private int numberOfPayrolls;
    private String status;

    public PayrollHistoryDto(PayrollHistory payrollHistory){
        this.payrollHistoryId = payrollHistory.getHistoryId();
        this.executionDate = payrollHistory.getExecutionDate();
        this.numberOfEmployees = payrollHistory.getNumberOfEmployees();
        this.numberOfPayrolls = payrollHistory.getNumberOfPayrolls();
        this.status = payrollHistory.getStatus().name();
    }
}
