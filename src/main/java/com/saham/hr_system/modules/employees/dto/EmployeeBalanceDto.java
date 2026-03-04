package com.saham.hr_system.modules.employees.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeBalanceDto {
    private int year;
    private double annualBalance; // the annual right
    private double accumulatedBalance; // the accumulated balance
    private double usedBalance; // the total days used
    private double remainderBalance; // the remainder balance
    private double previousYearBalance; // the balance from the previous year

}
