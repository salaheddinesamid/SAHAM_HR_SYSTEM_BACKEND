package com.saham.hr_system.modules.expenses.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ExpenseItemRequest {
    private LocalDate expenseDate;
    private String designation;
    private double amount;
}
