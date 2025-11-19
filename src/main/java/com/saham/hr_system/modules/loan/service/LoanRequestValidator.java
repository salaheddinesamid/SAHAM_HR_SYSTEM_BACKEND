package com.saham.hr_system.modules.loan.service;

import com.saham.hr_system.modules.loan.dto.LoanRequestDto;

public interface LoanRequestValidator {

    void validate(LoanRequestDto requestDto) throws Exception;
}
