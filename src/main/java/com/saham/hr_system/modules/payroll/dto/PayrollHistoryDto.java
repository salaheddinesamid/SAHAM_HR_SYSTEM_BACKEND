package com.saham.hr_system.modules.payroll.dto;
import com.saham.hr_system.modules.payroll.model.PayrollHistory;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PayrollHistoryDto {
    private long payrollHistoryId;
    private LocalDateTime executionDate;
    private int numberOfEmployees;
    private int succeededPayrolls;
    private int failedPayrolls;
    private int totalPayrolls;
    private int payrollMonth;
    private int payrollYear;
    private String status;

    public PayrollHistoryDto(PayrollHistory payrollHistory){
        this.payrollHistoryId = payrollHistory.getHistoryId();
        this.executionDate = payrollHistory.getExecutionDate();
        this.numberOfEmployees = payrollHistory.getNumberOfEmployees();
        this.succeededPayrolls = payrollHistory.getSucceededPayrolls();
        this.failedPayrolls = payrollHistory.getFailedPayrolls();
        this.totalPayrolls = payrollHistory.getTotalPayrolls();
        this.payrollMonth = payrollHistory.getPayrollMonth();
        this.payrollYear = payrollHistory.getPayrollYear();
        this.status = payrollHistory.getStatus().name();
    }
}
