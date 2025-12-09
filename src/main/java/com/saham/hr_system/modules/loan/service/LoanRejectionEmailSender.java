package com.saham.hr_system.modules.loan.service;

import com.saham.hr_system.modules.loan.model.LoanRequest;
import jakarta.mail.MessagingException;

public interface LoanRejectionEmailSender {

    void notifyEmployee(LoanRequest loanRequest) throws MessagingException;
}
