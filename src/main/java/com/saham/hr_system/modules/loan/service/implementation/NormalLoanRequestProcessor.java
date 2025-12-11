package com.saham.hr_system.modules.loan.service.implementation;

import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.loan.dto.LoanRequestDto;
import com.saham.hr_system.modules.loan.model.LoanRequest;
import com.saham.hr_system.modules.loan.model.LoanRequestStatus;
import com.saham.hr_system.modules.loan.model.LoanType;
import com.saham.hr_system.modules.loan.repository.LoanRequestRepository;
import com.saham.hr_system.modules.loan.service.LoanRequestProcessor;
import com.saham.hr_system.modules.loan.utils.LoanReferenceNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Component
public class NormalLoanRequestProcessor implements LoanRequestProcessor {

    private final LoanRequestRepository loanRequestRepository;
    private final LoanRequestValidatorImpl loanRequestValidator;
    private final LoanRequestEmailSenderImpl loanRequestEmailSender;
    private final LoanReferenceNumberGenerator loanReferenceNumberGenerator;

    @Autowired
    public NormalLoanRequestProcessor(LoanRequestRepository loanRequestRepository, LoanRequestValidatorImpl loanRequestValidator, LoanRequestEmailSenderImpl loanRequestEmailSender, LoanReferenceNumberGenerator loanReferenceNumberGenerator) {
        this.loanRequestRepository = loanRequestRepository;
        this.loanRequestValidator = loanRequestValidator;
        this.loanRequestEmailSender = loanRequestEmailSender;
        this.loanReferenceNumberGenerator = loanReferenceNumberGenerator;
    }

    @Override
    public boolean supports(String loanType) {
        return LoanType.NORMAL.equals(LoanType.valueOf(loanType)) || LoanType.ADVANCE.equals(LoanType.valueOf(loanType));
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

        String refNumber = loanReferenceNumberGenerator
                .generate(loanRequest);

        loanRequest.setReferenceNumber(refNumber); // set the reference number

        // notify the employee and HR ASYNC:
        CompletableFuture.runAsync(()->{
            try{
                loanRequestEmailSender.notifyEmployee(loanRequest);
                loanRequestEmailSender.notifyHR(loanRequest);
            }catch (RuntimeException e){
                e.printStackTrace();
            }
        });

        // save the loan request to the db:
        return loanRequestRepository.save(loanRequest);
    }
}
