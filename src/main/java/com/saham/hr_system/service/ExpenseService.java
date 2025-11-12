package com.saham.hr_system.service;

import com.saham.hr_system.dto.ExpenseRequestDto;
import com.saham.hr_system.dto.ExpenseResponseDto;

/**
 *
 */
public interface ExpenseService {

    /**
     * This function creates new expense in the system
     * @param expenseRequestDto
     * @return an expense response that contains all the information
     */
    ExpenseResponseDto newExpense(String email ,ExpenseRequestDto expenseRequestDto);
}
