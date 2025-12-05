package com.saham.hr_system.modules.loan.service.implementation;

import com.saham.hr_system.modules.loan.model.LoanRequest;
import com.saham.hr_system.modules.loan.service.LoanRejectionEmailSender;
import com.saham.hr_system.utils.OutlookEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

@Component
public class LoanRejectionEmailSenderImpl implements LoanRejectionEmailSender {

    @Autowired
    private OutlookEmailService outlookEmailService;

    @Autowired
    private TemplateEngine templateEngine;


    @Override
    public void notifyEmployee(LoanRequest loanRequest) {

    }
}
