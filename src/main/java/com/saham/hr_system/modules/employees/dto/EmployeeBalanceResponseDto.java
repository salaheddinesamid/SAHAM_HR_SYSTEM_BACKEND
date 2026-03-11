package com.saham.hr_system.modules.employees.dto;

import com.saham.hr_system.modules.employees.model.EmployeeBalance;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeBalanceResponseDto {
    private long id;
    private String fullName;
    private int year;
    private double annualBalance; // the annual right
    private double accumulatedBalance; // the accumulated balance
    private double usedBalance; // the total days used
    private double remainderBalance; // the remainder balance

    public EmployeeBalanceResponseDto(EmployeeBalance employeeBalance){
        this.id = employeeBalance.getBalanceId();
        this.fullName = employeeBalance.getEmployee().getFullName();
        this.year = employeeBalance.getYear();
        this.annualBalance = employeeBalance.getAnnualBalance();
        this.accumulatedBalance = employeeBalance.getAccumulatedBalance();
        this.usedBalance = employeeBalance.getUsedBalance();
        this.remainderBalance = employeeBalance.getRemainderBalance();
    }
}
