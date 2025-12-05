package com.saham.hr_system.modules.loan.service;

import com.saham.hr_system.modules.loan.dto.LoanResponseDto;

import java.util.List;

public interface LoanQueryService {

    List<LoanResponseDto> getAllLoans();
}
