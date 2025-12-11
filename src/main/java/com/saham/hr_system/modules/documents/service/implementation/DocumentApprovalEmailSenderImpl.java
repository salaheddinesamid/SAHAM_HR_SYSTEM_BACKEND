package com.saham.hr_system.modules.documents.service.implementation;

import com.saham.hr_system.modules.documents.model.DocumentRequest;
import com.saham.hr_system.modules.documents.service.DocumentApprovalEmailSender;
import com.saham.hr_system.utils.OutlookEmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class DocumentApprovalEmailSenderImpl implements DocumentApprovalEmailSender {

    @Autowired
    private OutlookEmailService outlookEmailService;

    @Autowired
    private TemplateEngine templateEngine;

    private final static String TO = "salaheddine.samid@medjoolstar.com";
    @Override
    public void notifyEmployee(DocumentRequest documentRequest) throws MessagingException {
        // Template variables
        Context context = new Context();
        context.setVariable("employeeName", documentRequest.getEmployee().getFullName());
        context.setVariable("documentsRequested", documentRequest.getDocuments());
        String htmlContent = templateEngine.process("document-request-approved-employee.html", context);

        outlookEmailService
                .sendEmail(
                        TO,
                        htmlContent,
                        "Votre demande de documents a été approuvée"
                );
    }
}
