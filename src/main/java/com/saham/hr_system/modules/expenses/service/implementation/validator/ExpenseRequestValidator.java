package com.saham.hr_system.modules.expenses.service.implementation.validator;

import com.saham.hr_system.modules.expenses.dto.ExpenseRequestDto;
import org.springframework.stereotype.Component;

@Component
public class ExpenseRequestValidator {

    public void validateExpenseRequest(ExpenseRequestDto expenseRequestDto) {

        if(expenseRequestDto.getExpenseItems().isEmpty()) {
            throw new IllegalArgumentException("Expense request must contain at least one expense item.");
        }
        if(expenseRequestDto.getLocation().equals("OUTSIDE_MOROCCO") && expenseRequestDto.getCurrency().equals("MAD")) {
            throw new IllegalArgumentException("Expenses outside Morocco cannot be in MAD currency.");
        }
        if(expenseRequestDto.getLocation().equals("INSIDE_MOROCCO") && !expenseRequestDto.getCurrency().equals("MAD")) {
            throw new IllegalArgumentException("Expenses inside Morocco cannot be in foreign currency.");
        }
    }
}
