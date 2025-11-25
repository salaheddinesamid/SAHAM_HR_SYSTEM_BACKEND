package com.saham.hr_system.modules.expenses.dto;

import com.saham.hr_system.modules.expenses.model.Expense;
import com.saham.hr_system.modules.expenses.model.ExpenseItem;
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
    private String motif;
    private double totalAmount;
    private String currency;
    private double exchangeRate;
    private String location;

    public ExpenseResponseDto(Expense expense) {
        this.id = expense.getId();
        this.issueBy = expense.getEmployee().getFullName();
        this.date = expense.getIssueDate();
        this.createdAt = expense.getCreatedAt();
        this.totalAmount = expense.getTotalAmount();
        this.currency = expense.getCurrency().toString();
        this.exchangeRate = expense.getExchangeRate();
        this.location = expense.getExpenseLocation().toString();
        this.motif = expense.getMotif();
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
    private boolean invoiced;
    ExpenseItemResponse(
            ExpenseItem expenseItem
    ){
        this.id = expenseItem.getId();
        this.date = expenseItem.getDate();
        this.designation = expenseItem.getDesignation();
        this.amount = expenseItem.getAmount();
        this.invoiced = expenseItem.isInvoiced();
    }
}
