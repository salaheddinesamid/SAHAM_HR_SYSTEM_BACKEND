package com.saham.hr_system.modules.loan.service;

import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.loan.dto.LoanRequestDto;
import com.saham.hr_system.modules.loan.model.LoanRequest;

/**
 *
 */
public interface LoanRequestProcessor {

    /**
     *
     * @return if the request is supported by the processor
     */
    boolean supports(String loanType);

    /**
     * Process loan requests.
     * @param requestDto
     * @return a loan request.
     */
    LoanRequest process(Employee employee, LoanRequestDto requestDto) throws Exception;
}
