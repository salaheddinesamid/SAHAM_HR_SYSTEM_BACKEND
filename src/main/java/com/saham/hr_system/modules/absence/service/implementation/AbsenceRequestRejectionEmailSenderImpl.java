package com.saham.hr_system.modules.absence.service.implementation;

import com.saham.hr_system.modules.absence.model.AbsenceRequest;
import com.saham.hr_system.modules.absence.service.AbsenceRequestRejectionEmailSender;
import com.saham.hr_system.utils.OutlookEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class AbsenceRequestRejectionEmailSenderImpl implements AbsenceRequestRejectionEmailSender {

    @Autowired
    private OutlookEmailService outlookEmailService;

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public void notifyEmployee(AbsenceRequest absenceRequest) {
        //String to = leaveRequest.getEmployee().getEmail();

        String to = "salaheddine.samid@medjoolstar.com";
        //helper.setTo("salaheddine.samid@medjoolstar.com"); // for testing purposes

        // Template variables
        Context context = new Context();
        context.setVariable("manager", absenceRequest.getEmployee().getManager().getFullName());
        context.setVariable("type", absenceRequest.getType().toString());
        context.setVariable("startDate", absenceRequest.getStartDate());
        context.setVariable("endDate", absenceRequest.getEndDate());
        context.setVariable("referenceNumber", absenceRequest.getReferenceNumber());
        context.setVariable("logoUrl", "https://yourpublicurl.com/logo.png");

        String htmlContent = templateEngine.process("leave-request-rejected-employee.html", context);
        outlookEmailService.sendEmail(
                to,
                htmlContent,
                "Votre demande de congé est rejeté par le manager"
        );
        System.out.println("Leave rejection email sent to: " + to);
    }
}
