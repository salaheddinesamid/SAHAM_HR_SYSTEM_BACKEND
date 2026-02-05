package com.saham.hr_system.modules.loan.service.implementation;

import com.saham.hr_system.modules.loan.dto.LoanRequestDto;
import com.saham.hr_system.modules.loan.exception.InvalidCollectionDateException;
import com.saham.hr_system.modules.loan.exception.InvalidLoanAmountException;
import com.saham.hr_system.modules.loan.exception.InvalidLoanTypeException;
import com.saham.hr_system.modules.loan.service.LoanRequestValidator;
import org.springframework.stereotype.Component;

@Component
public class LoanRequestValidatorImpl implements LoanRequestValidator {
    @Override
    public void validate(LoanRequestDto requestDto) throws Exception {
        if(requestDto.getLoanType() == null){
            throw new InvalidLoanTypeException("The Loan Type is invalid");
        }
        if(requestDto.getAmount() == 0){
            throw new InvalidLoanAmountException("The amount requested is invalid, it must be greater than zero");
        }
        if(requestDto.getDateOfCollection() == null){
            throw new InvalidCollectionDateException("The date of collection is invalid");
        }
    }
}
