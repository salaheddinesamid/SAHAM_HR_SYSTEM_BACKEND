package com.saham.hr_system.modules.documents.service.implementation;

import com.saham.hr_system.modules.documents.model.DocumentRequest;
import com.saham.hr_system.modules.documents.service.DocumentRequestEmailSender;
import com.saham.hr_system.modules.leave.utils.HRFetcherUtils;
import com.saham.hr_system.utils.OutlookEmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

@Component
public class DocumentRequestEmailSenderImpl implements DocumentRequestEmailSender {

    @Autowired
    private OutlookEmailService outlookEmailService;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private HRFetcherUtils hrFetcherUtils;

    private final static String TO = "salaheddine.samid@medjoolstar.com";
    @Override
    public void notifyEmployee(DocumentRequest documentRequest) throws MessagingException {

        // Template variables
        Context context = new Context();
        context.setVariable("employeeName", documentRequest.getEmployee().getFullName());
        context.setVariable("documentsRequested", documentRequest.getDocuments());
        String htmlContent = templateEngine.process("document-requested-employee.html", context);

        outlookEmailService
                .sendEmail(
                        TO,
                        htmlContent,
                        "Votre demande de documents a été soumise avec succès"
                );
    }

    @Override
    public void notifyHR(DocumentRequest documentRequest) throws MessagingException {
        List<String> hrEmails = hrFetcherUtils.fetchHREmail();
        hrEmails.forEach(hrEmail -> {
            // Template variables
            Context context = new Context();
            context.setVariable("documentsRequested", documentRequest.getDocuments());
            String htmlContent = templateEngine.process("document-requested-employee.html", context);

            outlookEmailService
                    .sendEmail(
                            TO,
                            htmlContent,
                            "Nouvelle demande des documents à valider"
                    );
        });
    }
}
