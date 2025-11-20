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
     * @param reason
     */
    void rejectLoanRequest(Long loanRequestId, String reason);
}
