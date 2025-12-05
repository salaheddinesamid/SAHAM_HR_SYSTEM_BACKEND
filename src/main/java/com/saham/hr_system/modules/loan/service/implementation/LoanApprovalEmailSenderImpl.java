package com.saham.hr_system.modules.loan.service.implementation;

import com.saham.hr_system.modules.loan.model.LoanRequest;
import com.saham.hr_system.modules.loan.service.LoanApprovalEmailSender;
import com.saham.hr_system.modules.loan.utils.LoanTypeMapper;
import com.saham.hr_system.utils.OutlookEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class LoanApprovalEmailSenderImpl implements LoanApprovalEmailSender {

    @Autowired
    private OutlookEmailService outlookEmailService;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private LoanTypeMapper loanTypeMapper;

    private final static String TO = "salaheddine.samid@medjoolstar.com";


    @Override
    public void notifyEmployee(LoanRequest loanRequest) {
        //String TO = leaveRequest.getEmployee().getEmail();

        // Template engine variables:
        Context context = new Context();
        String type = loanTypeMapper.mapLoanType(loanRequest.getType().toString());
        context.setVariable("employeeName", loanRequest.getEmployee().getFullName());
        context.setVariable("loanType", type);
        context.setVariable("issueDate", loanRequest.getIssueDate());
        context.setVariable("amount", loanRequest.getAmount());
        context.setVariable("motif", loanRequest.getMotif());
        context.setVariable("logoUrl","");
        // generate HTML content:
        String htmlContent = templateEngine.process("loan-approved-employee.html", context);
        outlookEmailService.sendEmail(
                TO,
                htmlContent,
                "Votre demande de pret/avance est approuvée par le service RH"
        );
        System.out.println("Loan approval email sent to:" + TO);
    }
}
