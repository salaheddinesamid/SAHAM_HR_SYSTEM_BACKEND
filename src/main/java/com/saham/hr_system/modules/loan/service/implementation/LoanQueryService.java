package com.saham.hr_system.modules.loan.service.implementation;

import com.saham.hr_system.modules.loan.model.LoanResponseDto;
import com.saham.hr_system.modules.loan.service.LoanQueryService;

import java.util.List;

public class LoanQueryServiceImpl implements LoanQueryService {
    @Override
    public List<LoanResponseDto> getAllLoans() {
        return List.of();
    }
}
