package com.saham.hr_system.modules.loan.service.implementation;
import com.saham.hr_system.modules.leave.utils.HRFetcherUtils;
import com.saham.hr_system.modules.leave.utils.LocalDateMapper;
import com.saham.hr_system.modules.loan.model.LoanRequest;
import com.saham.hr_system.modules.loan.service.LoanRequestEmailSender;
import com.saham.hr_system.modules.loan.utils.LoanTypeMapper;
import com.saham.hr_system.utils.OutlookEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

@Component
public class LoanRequestEmailSenderImpl implements LoanRequestEmailSender {

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private OutlookEmailService outlookEmailService;

    @Autowired
    private LoanTypeMapper loanTypeMapper;

    @Autowired
    private LocalDateMapper localDateMapper;

    @Autowired
    private HRFetcherUtils hrFetcherUtils;

    @Override
    public void notifyEmployee(LoanRequest loanRequest) {
        String to = loanRequest.getEmployee().getEmail();

        // Template variables
        Context context = new Context();
        String typeMapped = loanTypeMapper.mapLoanType(loanRequest.getType().toString());

        context.setVariable("employeeName",loanRequest.getEmployee().getFullName());
        context.setVariable("loanType", typeMapped);
        context.setVariable("issueDate", localDateMapper.mapToFrenchFormat(loanRequest.getIssueDate().toLocalDate()));
        context.setVariable("amount", loanRequest.getAmount());
        context.setVariable("motif", loanRequest.getMotif());
        context.setVariable("logoUrl", "https://yourpublicurl.com/logo.png");

        String htmlContent = templateEngine.process("loan-requested-employee.html", context);

        outlookEmailService.sendEmail(
                to,
                htmlContent,
                "Votre demande de pret/avance a été soumise avec succès"
        );
        System.out.println("Loan request approval email sent to: " + to);
    }

    @Override
    public void notifyHR(LoanRequest loanRequest) {
        List<String> hrEmails = hrFetcherUtils.fetchHREmail();

        // Template variables
        Context context = new Context();
        String typeMapped = loanTypeMapper.mapLoanType(loanRequest.getType().toString());

        context.setVariable("employeeName",loanRequest.getEmployee().getFullName());
        context.setVariable("loanType", typeMapped);
        context.setVariable("issueDate", localDateMapper.mapToFrenchFormat(loanRequest.getIssueDate().toLocalDate()));
        context.setVariable("amount", loanRequest.getAmount());
        context.setVariable("motif", loanRequest.getMotif());
        context.setVariable("logoUrl", "https://yourpublicurl.com/logo.png");

        String htmlContent = templateEngine.process("loan-requested-hr.html", context);

        for(String email : hrEmails){
            outlookEmailService.sendEmail(
                    email,
                    htmlContent,
                    "Nouvelle demande de pret/avance à valider"
            );
            System.out.println("Loan request approval email sent to: " + email);
        }

    }
}
