package com.saham.hr_system.modules.expenses.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ExpenseRequestDto {

    private String motif;
    private double balance;
    private List<ExpenseItemRequest> expenseItems;
    private LocalDate issueDate;
}
