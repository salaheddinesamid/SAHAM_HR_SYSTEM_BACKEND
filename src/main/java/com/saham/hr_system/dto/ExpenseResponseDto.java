package com.saham.hr_system.dto;

import com.saham.hr_system.model.Expense;
import com.saham.hr_system.model.ExpenseItem;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ExpenseResponseDto {

    private Long id;
    private String issueBy;
    private LocalDate date;
    private LocalDateTime createdAt;
    private List<ExpenseItemResponse> expenseItems;

    public ExpenseResponseDto(Expense expense) {
        this.id = expense.getId();
        this.issueBy = expense.getEmployee().getFullName();
        this.date = expense.getIssueDate();
        this.createdAt = expense.getCreatedAt();
        this.expenseItems =
                expense.getItems().stream()
                        .map(ExpenseItemResponse::new).toList();
    }
}

@Data
class ExpenseItemResponse{
    private Long id;
    private LocalDate date;
    private String designation;
    private double amount;
    ExpenseItemResponse(
            ExpenseItem expenseItem
    ){
        this.id = expenseItem.getId();
        this.date = expenseItem.getDate();
        this.designation = expenseItem.getDesignation();
        this.amount = expenseItem.getAmount();
    }
}
