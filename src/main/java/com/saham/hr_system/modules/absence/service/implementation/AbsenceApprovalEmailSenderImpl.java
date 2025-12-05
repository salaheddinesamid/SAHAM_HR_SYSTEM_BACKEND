package com.saham.hr_system.modules.absence.service.implementation;

import com.saham.hr_system.modules.absence.model.Absence;
import com.saham.hr_system.modules.absence.service.AbsenceApprovalEmailSender;
import com.saham.hr_system.utils.OutlookEmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class AbsenceApprovalEmailSenderImpl implements AbsenceApprovalEmailSender {

    @Autowired
    private OutlookEmailService outlookEmailService;

    @Autowired
    private TemplateEngine templateEngine;

    //private final static String TO = "salaheddine.samid@medjoolstar.com"; // for testing purposes

    @Override
    public void notifyEmployee(Absence absence) throws MessagingException {
        String TO = absence.getEmployee().getEmail();

        // Template engine variables:
        Context context = new Context();
        context.setVariable("employeeName", absence.getEmployee().getFullName());
        context.setVariable("type", absence.getType().toString());
        context.setVariable("startDate", absence.getStartDate());
        context.setVariable("endDate", absence.getEndDate());
        context.setVariable("referenceNumber", absence.getReferenceNumber());
        context.setVariable("totalDays", absence.getTotalDays());
        context.setVariable("logoUrl","");
        // generate HTML content:
        String htmlContent = templateEngine.process("absence-approved-employee.html", context);
        outlookEmailService.sendEmail(
                TO,
                htmlContent,
                "Votre demande d'absence est approuvée par le service RH"
        );
        System.out.println("Absence approval email sent to:" + TO);
    }

    @Override
    public void notifyManager(Absence absence) throws MessagingException {
        String TO = absence.getEmployee().getManager().getEmail();

        // Template engine variables:
        Context context = new Context();
        context.setVariable("managerName", absence.getEmployee().getManager().getFullName());
        context.setVariable("employeeName", absence.getEmployee().getManager().getFullName());
        context.setVariable("type", absence.getStartDate());
        context.setVariable("startDate", absence.getStartDate());
        context.setVariable("endDate", absence.getEndDate());
        context.setVariable("totalDays", absence.getTotalDays());
        context.setVariable("referenceNumber", absence.getReferenceNumber());

        context.setVariable("logoUrl","");
        // generate HTML content:
        String htmlContent = templateEngine.process("absence-approved-manager.html", context);
        outlookEmailService.sendEmail(
                TO,
                htmlContent,
                "Demande d'absence approuvée par le service RH"
        );
        System.out.println("Absence approval email sent to:" + TO);
    }
}
