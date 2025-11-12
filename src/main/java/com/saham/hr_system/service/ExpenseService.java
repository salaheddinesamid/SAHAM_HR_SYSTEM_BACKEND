package com.saham.hr_system.service;

/**
 *
 */
public interface ExpenseService {

    /**
     * This function creates new expense in the system
     * @param expenseRequestDto
     * @return an expense response that contains all the information
     */
    ExpenseResponseDto newExpense(ExpenseRequestDto expenseRequestDto);
}
