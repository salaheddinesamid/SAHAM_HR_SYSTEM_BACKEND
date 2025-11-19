package com.saham.hr_system.modules.loan.service.implementation;

import com.saham.hr_system.modules.loan.dto.LoanRequestDto;
import com.saham.hr_system.modules.loan.service.LoanRequestValidator;
import org.springframework.stereotype.Component;

@Component
public class LoanRequestValidatorImpl implements LoanRequestValidator {
    @Override
    public void validate(LoanRequestDto requestDto) throws Exception {
        if(requestDto.getLoanType() == null){
            throw new Exception("");
        }
        if(requestDto.getAmount() == 0){
            throw new Exception("");
        }
    }
}
