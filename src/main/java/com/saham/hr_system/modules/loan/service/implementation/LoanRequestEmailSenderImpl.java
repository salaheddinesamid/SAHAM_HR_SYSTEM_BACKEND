package com.saham.hr_system.modules.loan.service.implementation;
import com.saham.hr_system.modules.loan.model.LoanRequest;
import com.saham.hr_system.modules.loan.service.LoanRequestEmailSender;
import com.saham.hr_system.modules.loan.utils.LoanTypeMapper;
import com.saham.hr_system.utils.OutlookEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class LoanRequestEmailSenderImpl implements LoanRequestEmailSender {

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private OutlookEmailService outlookEmailService;

    @Autowired
    private LoanTypeMapper loanTypeMapper;

    private final static String TO = "salaheddine.samid@medjoolstar.com";

    @Override
    public void notifyEmployee(LoanRequest loanRequest) {
        //String TO = leaveRequest.getEmployee().getEmail();

        // Template variables
        Context context = new Context();
        String typeMapped = loanTypeMapper.mapLoanType(loanRequest.getType().toString());

        context.setVariable("loanType", typeMapped);
        context.setVariable("issueDate", loanRequest.getIssueDate());
        context.setVariable("amount", loanRequest.getAmount());
        context.setVariable("motif", loanRequest.getMotif());
        context.setVariable("logoUrl", "https://yourpublicurl.com/logo.png");

        String htmlContent = templateEngine.process("loan-requested-employee.html", context);

        outlookEmailService.sendEmail(
                TO,
                htmlContent,
                "Votre demande de pret/avance a été soumise avec succès"
        );
        System.out.println("Loan request approval email sent to: " + TO);
    }

    @Override
    public void notifyHR(LoanRequest loanRequest) {
        //String TO = leaveRequest.getEmployee().getEmail();

        // Template variables
        Context context = new Context();
        String typeMapped = loanTypeMapper.mapLoanType(loanRequest.getType().toString());

        context.setVariable("loanType", typeMapped);
        context.setVariable("issueDate", loanRequest.getIssueDate());
        context.setVariable("amount", loanRequest.getAmount());
        context.setVariable("motif", loanRequest.getMotif());
        context.setVariable("logoUrl", "https://yourpublicurl.com/logo.png");

        String htmlContent = templateEngine.process("loan-requested-hr.html", context);

        outlookEmailService.sendEmail(
                TO,
                htmlContent,
                "Nouvelle demande de pret/avance à valider"
        );
        System.out.println("Loan request approval email sent to: " + TO);
    }
}
