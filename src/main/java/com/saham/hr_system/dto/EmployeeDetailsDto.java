package com.saham.hr_system.dto;

import com.saham.hr_system.model.Employee;
import com.saham.hr_system.model.EmployeeBalance;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class EmployeeDetailsDto {

    private String fullName;
    private String email;
    private String entity;
    private String occupation;
    private String matriculation;
    private LocalDate joinDate;
    private BalanceDetails balanceDetails;

    public EmployeeDetailsDto(Employee employee) {
        this.fullName = String.format("%s %s", employee.getFirstName(), employee.getLastName());
        this.email = employee.getEmail();
        this.entity = employee.getEntity();
        this.joinDate = employee.getJoinDate();
        this.occupation = employee.getMatriculation();
        this.balanceDetails = new BalanceDetails(employee.getBalance());
    }
}

@Data
class BalanceDetails{
    int year;
    double initialBalance;
    double accumulatedBalance;
    double usedBalance;
    double leftBalance;
    LocalDateTime lastUpdated;

    public BalanceDetails(
            EmployeeBalance employeeBalance
    ) {
        this.year = employeeBalance.getYear();
        this.accumulatedBalance = employeeBalance.getAccumulatedBalance();
        this.initialBalance = employeeBalance.getInitialBalance();
        this.usedBalance = employeeBalance.getUsedBalance();
        this.leftBalance = employeeBalance.getDaysLeft();
        this.lastUpdated = employeeBalance.getLastUpdated();
    }
}
