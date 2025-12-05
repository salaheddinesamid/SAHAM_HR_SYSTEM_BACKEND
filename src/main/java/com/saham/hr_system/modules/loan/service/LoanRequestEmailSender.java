package com.saham.hr_system.modules.loan.service;

import com.saham.hr_system.modules.loan.model.LoanRequest;

public interface LoanRequestEmailSender {

    void notifyEmployee(LoanRequest loanRequest);
    void notifyHR(LoanRequest loanRequest);
}
