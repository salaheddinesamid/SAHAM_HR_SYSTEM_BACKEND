package com.saham.hr_system.modules.loan.dto;

import com.saham.hr_system.modules.loan.model.LoanRequest;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class LoanRequestResponseDto {
    private Long id;
    private String requestedBy;
    private double amount;
    private String paymentModularity;
    private Integer numberOfDeadlines;
    private LocalDate collectionDate;
    private LocalDateTime issueDate;
    private boolean isApprovedByHr;
    private boolean isApprovedByFinanceDepartment;
    private String status;

    public LoanRequestResponseDto(
            LoanRequest loanRequest
    ){
        this.id = loanRequest.getRequestId();
        this.requestedBy = loanRequest.getEmployee().getFullName();
        this.amount = loanRequest.getAmount();
        this.paymentModularity = loanRequest.getPaymentModularity();
        this.numberOfDeadlines = loanRequest.getNumberOfDeadlines();
        this.collectionDate = loanRequest.getCollectionDate();
        this.isApprovedByHr = loanRequest.isApprovedByHrDepartment();
        this.isApprovedByFinanceDepartment = loanRequest.isApprovedByFinanceDepartment();
        this.status = loanRequest.getStatus().toString();
        this.issueDate = loanRequest.getIssueDate();
    }
}
