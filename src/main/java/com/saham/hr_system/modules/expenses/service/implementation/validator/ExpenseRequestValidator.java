package com.saham.hr_system.modules.expenses.service.implementation.validator;

import com.saham.hr_system.modules.expenses.dto.ExpenseItemRequest;
import com.saham.hr_system.modules.expenses.dto.ExpenseRequestDto;
import org.springframework.stereotype.Component;

@Component
public class ExpenseRequestValidator {

    public void validateExpenseRequest(ExpenseRequestDto expenseRequestDto) {

        if(expenseRequestDto.getExpenseItems().isEmpty()) {
            throw new IllegalArgumentException("Expense request must contain at least one expense item.");
        }
    }

    public static void validateExpenseItemRequest(ExpenseItemRequest expenseItemRequest) {

    }
}
