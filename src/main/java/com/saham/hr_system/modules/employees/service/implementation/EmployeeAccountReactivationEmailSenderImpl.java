package com.saham.hr_system.modules.employees.service.implementation;

import com.saham.hr_system.modules.employees.service.EmployeeAccountReactivationEmailSender;
import com.saham.hr_system.utils.OutlookEmailService;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Service
public class EmployeeAccountReactivationEmailSenderImpl implements EmployeeAccountReactivationEmailSender {
    private final OutlookEmailService outlookEmailService;
    private final TemplateEngine templateEngine;

    public EmployeeAccountReactivationEmailSenderImpl(OutlookEmailService outlookEmailService, TemplateEngine templateEngine) {
        this.outlookEmailService = outlookEmailService;
        this.templateEngine = templateEngine;
    }


    @Override
    public void sendReactivationEmail(String employeeEmail, String link) {

        Context context  = new Context();
        context.setVariable("link", link);
        // generate HTML content:
        String htmlContent = templateEngine.process("employee-account-reactivation.html", context);
        outlookEmailService.sendEmail(
                employeeEmail,
                htmlContent,
                "Réactivation de votre compte employé"
        );
         System.out.println("Account reactivation email sent to:" + employeeEmail);
    }
}
