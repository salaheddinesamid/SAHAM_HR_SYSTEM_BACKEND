package com.saham.hr_system.modules.employees.dto;

import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.model.EmployeeBalance;
import com.saham.hr_system.modules.employees.model.Role;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class EmployeeDetailsDto {

    private String fullName;
    private String email;
    private String entity;
    private String occupation;
    private String matriculation;
    private LocalDate joinDate;
    private BalanceDetails balanceDetails;
    private List<String> roles;
    private String managerName;

    public EmployeeDetailsDto(Employee employee, EmployeeBalance balance) {
        this.fullName = String.format("%s %s", employee.getFirstName(), employee.getLastName());
        this.email = employee.getEmail();
        this.entity = employee.getEntity();
        this.joinDate = employee.getJoinDate();
        this.occupation = employee.getOccupation();
        this.matriculation = employee.getMatriculation();
        this.balanceDetails = new BalanceDetails(balance);
        this.roles = employee.getRoles().stream().map(Role::getRoleName).toList();
        this.managerName = employee.getManager() != null ?
                String.format("%s %s", employee.getManager().getFirstName(), employee.getManager().getLastName())
                : null;
    }
    public EmployeeDetailsDto(Employee employee) {
        this.fullName = String.format("%s %s", employee.getFirstName(), employee.getLastName());
        this.email = employee.getEmail();
        this.entity = employee.getEntity();
        this.joinDate = employee.getJoinDate();
        this.occupation = employee.getMatriculation();
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
