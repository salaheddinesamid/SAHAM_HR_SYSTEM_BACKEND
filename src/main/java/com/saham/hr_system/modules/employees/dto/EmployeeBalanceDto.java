package com.saham.hr_system.modules.employees.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/** * EmployeeBalanceDto is a Data Transfer Object (DTO) that encapsulates the balance information of an employee for a specific year.
 * It includes details about the annual balance, accumulated balance, used balance, remainder balance, and previous year's balance.
 */
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
