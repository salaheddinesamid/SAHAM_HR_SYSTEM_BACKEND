package com.saham.hr_system.modules.loan.service;

import com.saham.hr_system.modules.loan.dto.LoanRequestDto;
import com.saham.hr_system.modules.loan.dto.LoanRequestResponseDto;

import java.util.List;

public interface LoanService {

    /**
     * This function handles loan requests for employees.
     * @param email
     * @param requestDto
     */
    void requestLoan(String email, LoanRequestDto requestDto) throws Exception;
}
