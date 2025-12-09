package com.saham.hr_system.modules.loan.service.implementation;

import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.loan.model.Loan;
import com.saham.hr_system.modules.loan.model.LoanRequest;
import com.saham.hr_system.modules.loan.model.LoanRequestStatus;
import com.saham.hr_system.modules.loan.repository.LoanRepository;
import com.saham.hr_system.modules.loan.repository.LoanRequestRepository;
import com.saham.hr_system.modules.loan.service.LoanApproval;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Component
public class LoanApprovalImpl implements LoanApproval {

    private final LoanRequestRepository loanRequestRepository;
    private final LoanRepository loanRepository;
    private final LoanApprovalEmailSenderImpl loanApprovalEmailSender;
    private final LoanRejectionEmailSenderImpl loanRejectionEmailSender;

    @Autowired
    public LoanApprovalImpl(LoanRequestRepository loanRequestRepository, LoanRepository loanRepository, LoanApprovalEmailSenderImpl loanApprovalEmailSender, LoanRejectionEmailSenderImpl loanRejectionEmailSender) {
        this.loanRequestRepository = loanRequestRepository;
        this.loanRepository = loanRepository;
        this.loanApprovalEmailSender = loanApprovalEmailSender;
        this.loanRejectionEmailSender = loanRejectionEmailSender;
    }

    @Override
    @Transactional
    public void approveLoanRequest(Long loanRequestId) {
        // Fetch the loan request from db:
        LoanRequest loanRequest =
                loanRequestRepository.findById(loanRequestId).orElseThrow();

        // Fetch the employee from the loan request:
        Employee employee = loanRequest.getEmployee();
        // approve the request:
        loanRequest.setApprovedByHrDepartment(true);
        loanRequest.setApprovedByFinanceDepartment(true);
        loanRequest.setStatus(LoanRequestStatus.APPROVED);

        // create loan in the database:
        Loan loan = new Loan();
        loan.setApprovalDate(LocalDateTime.now());
        loan.setEmployee(employee);
        loan.setType(loanRequest.getType());
        loan.setAmount(loanRequest.getAmount());
        loan.setIssueDate(loanRequest.getIssueDate());

        // save the loan request:
        loanRequestRepository.save(loanRequest);

        // save the loan:
        loanRepository.save(loan);

        // notify the employee
        CompletableFuture.runAsync(() -> {
            try{
                loanApprovalEmailSender.notifyEmployee(loanRequest);
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        });

    }

    @Override
    public void rejectLoanRequest(Long loanRequestId) {
        // Fetch the loan request from db:
        LoanRequest loanRequest =
                loanRequestRepository.findById(loanRequestId).orElseThrow();

        // approve the request:
        loanRequest.setApprovedByHrDepartment(false);
        loanRequest.setApprovedByFinanceDepartment(false);

        loanRequest.setStatus(LoanRequestStatus.REJECTED);

        // save the loan request:
        loanRequestRepository.save(loanRequest);

        // notify the employee:
        CompletableFuture.runAsync(()->{
            try{
                loanRejectionEmailSender.notifyEmployee(loanRequest);
            }catch (MessagingException e){
                throw new RuntimeException(e);
            }
        });
    }
}
