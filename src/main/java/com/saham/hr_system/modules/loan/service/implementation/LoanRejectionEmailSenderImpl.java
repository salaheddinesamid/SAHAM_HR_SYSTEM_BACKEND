package com.saham.hr_system.modules.loan.service.implementation;

import com.saham.hr_system.modules.loan.model.LoanRequest;
import com.saham.hr_system.modules.loan.service.LoanRejectionEmailSender;
import com.saham.hr_system.utils.OutlookEmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class LoanRejectionEmailSenderImpl implements LoanRejectionEmailSender {

    @Autowired
    private OutlookEmailService outlookEmailService;

    @Autowired
    private TemplateEngine templateEngine;

    private final static String TO = "salaheddine.samid@medjoolstar.com";

    @Override
    public void notifyEmployee(LoanRequest loanRequest) throws MessagingException {
        Context context = new Context();
        context.setVariable("employeeName",loanRequest.getEmployee().getFullName());
        context.setVariable("loanType",loanRequest.getType().toString());
        context.setVariable("issueDate",loanRequest.getIssueDate());
        context.setVariable("amount",loanRequest.getAmount());
        context.setVariable("motif",loanRequest.getMotif());

        String htmlContent = templateEngine.process("loan-rejected-employee.html", context);
        outlookEmailService.sendEmail(
                TO,
                htmlContent,
                "Votre demande de prêt/avance a été rejetée par le service RH"
        );
    }
}
