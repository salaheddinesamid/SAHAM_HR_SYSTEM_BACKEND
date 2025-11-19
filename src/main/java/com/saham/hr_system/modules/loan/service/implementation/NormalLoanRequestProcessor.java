package com.saham.hr_system.modules.loan.service.implementation;

import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.modules.loan.dto.LoanRequestDto;
import com.saham.hr_system.modules.loan.model.LoanRequest;
import com.saham.hr_system.modules.loan.model.LoanRequestStatus;
import com.saham.hr_system.modules.loan.model.LoanType;
import com.saham.hr_system.modules.loan.repository.LoanRequestRepository;
import com.saham.hr_system.modules.loan.service.LoanRequestProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class NormalLoanRequestProcessor implements LoanRequestProcessor {

    private final LoanRequestRepository loanRequestRepository;
    private final LoanRequestValidatorImpl loanRequestValidator;

    @Autowired
    public NormalLoanRequestProcessor(LoanRequestRepository loanRequestRepository, LoanRequestValidatorImpl loanRequestValidator) {
        this.loanRequestRepository = loanRequestRepository;
        this.loanRequestValidator = loanRequestValidator;
    }

    @Override
    public boolean supports(String loanType) {
        return LoanType.NORMAL.equals(LoanType.valueOf(loanType));
    }

    @Override
    public LoanRequest process(Employee employee,LoanRequestDto requestDto) throws Exception {
        // validate the request:
        loanRequestValidator.validate(requestDto);

        // process the loan request:
        LoanRequest loanRequest = new LoanRequest();
        loanRequest.setAmount(requestDto.getAmount());
        loanRequest.setEmployee(employee);
        loanRequest.setMotif(requestDto.getMotif());
        loanRequest.setIssueDate(LocalDateTime.now());
        loanRequest.setApprovedByHrDepartment(false);
        loanRequest.setApprovedByHrDepartment(true);
        loanRequest.setType(LoanType.valueOf(requestDto.getLoanType()));
        loanRequest.setStatus(LoanRequestStatus.IN_PROCESS);

        // save the loan request to the db:
        return loanRequestRepository.save(loanRequest);
    }
}
