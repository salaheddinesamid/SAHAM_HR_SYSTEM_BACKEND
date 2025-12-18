package com.saham.hr_system.modules.administration.dto;

import lombok.Data;

@Data
public class EmployeeBalanceDto{

    private int year;
    private double annualBalance; // the annual right
    private double currentBalance; // the current balance
    private double accumulatedBalance; // the accumulated balance
    private double usedBalance; // the total days used
}