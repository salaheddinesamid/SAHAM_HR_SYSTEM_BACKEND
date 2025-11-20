package com.saham.hr_system.modules.loan.service;

public interface LoanApproval {
    /**
     *
     * @param loanRequestId
     */
    void approveLoanRequest(Long loanRequestId);

    /**
     *
     * @param loanRequestId
     */
    void rejectLoanRequest(Long loanRequestId);
}
