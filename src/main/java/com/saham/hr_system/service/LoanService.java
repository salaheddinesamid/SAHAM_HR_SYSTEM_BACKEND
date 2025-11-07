package com.saham.hr_system.service;

import com.saham.hr_system.dto.LoanRequestDto;

public interface LoanService {

    /**
     * This function handles loan requests for employees.
     * @param email
     * @param requestDto
     */
    void requestLoan(String email, LoanRequestDto requestDto);
}
