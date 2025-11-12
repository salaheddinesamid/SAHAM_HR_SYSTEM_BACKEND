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
    void requestLoan(String email, LoanRequestDto requestDto);

    /**
     * This function retrieves all loan requests made by a specific employee.
     * @param email
     * @return list of loan requests.
     */
    List<LoanRequestResponseDto> getAllEmployeeRequests(String email);

    /**
     * This function fetches all the loan requests made by all employees
     * @return list of loan requests.
     */
    List<LoanRequestResponseDto> getAllLoanRequests();
}
