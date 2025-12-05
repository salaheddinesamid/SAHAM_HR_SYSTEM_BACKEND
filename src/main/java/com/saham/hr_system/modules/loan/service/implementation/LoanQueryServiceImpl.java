package com.saham.hr_system.modules.loan.service.implementation;

import com.saham.hr_system.modules.loan.dto.LoanResponseDto;
import com.saham.hr_system.modules.loan.service.LoanQueryService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoanQueryServiceImpl implements LoanQueryService {
    @Override
    public List<LoanResponseDto> getAllLoans() {
        return List.of();
    }
}
