package com.saham.hr_system.modules.auth.service.implementation;

import com.saham.hr_system.modules.auth.service.ResetPasswordEmailSender;
import com.saham.hr_system.utils.OutlookEmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class EmployeeResetPasswordEmailSender implements ResetPasswordEmailSender {
    @Value("${frontend.url}")
    private String redirectionUrl;
    private final TemplateEngine templateEngine;
    private final OutlookEmailService outlookEmailService;

    @Autowired
    public EmployeeResetPasswordEmailSender(TemplateEngine templateEngine, OutlookEmailService outlookEmailService) {
        this.templateEngine = templateEngine;
        this.outlookEmailService = outlookEmailService;
    }

    @Override
    public void sendEmail(String recipientEmail, String resetToken) throws MessagingException {
        Context context = new Context();
        String resetLink = String.format("%s%s", redirectionUrl, "/reset-password?token=" + resetToken);
        context.setVariable("resetLink", resetLink);

        String content = templateEngine.process("password-reinitialization.html",context);
        outlookEmailService.sendEmail(recipientEmail, content, "");
    }
}
