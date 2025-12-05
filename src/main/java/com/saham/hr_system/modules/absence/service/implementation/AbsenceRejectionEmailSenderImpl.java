package com.saham.hr_system.modules.absence.service.implementation;

import com.saham.hr_system.modules.absence.model.AbsenceRequest;
import com.saham.hr_system.modules.absence.service.AbsenceRejectionEmailSender;
import com.saham.hr_system.utils.OutlookEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class AbsenceRejectionEmailSenderImpl implements AbsenceRejectionEmailSender {

    @Autowired
    private OutlookEmailService outlookEmailService;

    @Autowired
    private TemplateEngine templateEngine;

    //private final static String TO = "";
    @Override
    public void notifyEmployee(AbsenceRequest absenceRequest) {
        String TO = absenceRequest.getEmployee().getEmail();

        // Template engine variables:
        Context context = new Context();
        context.setVariable("employeeName", absenceRequest.getEmployee().getFullName());
        context.setVariable("type", absenceRequest.getType().toString());
        context.setVariable("startDate", absenceRequest.getStartDate());
        context.setVariable("endDate", absenceRequest.getEndDate());
        context.setVariable("referenceNumber", absenceRequest.getReferenceNumber());

        context.setVariable("logoUrl","");
        // generate HTML content:
        String htmlContent = templateEngine.process("leave-rejected-employee.html", context);
        outlookEmailService.sendEmail(
                TO,
                htmlContent,
                "Votre demande d'absence est rejetée par le service RH"
        );
        System.out.println("Leave rejection email sent to:" + TO);
    }

    @Override
    public void notifyManager(AbsenceRequest absenceRequest) {
        String TO = absenceRequest.getEmployee().getManager().getEmail();

        // Template engine variables:
        Context context = new Context();
        context.setVariable("managerName", absenceRequest.getEmployee().getManager().getFullName());
        context.setVariable("employeeName", absenceRequest.getEmployee().getManager().getFullName());
        context.setVariable("type", absenceRequest.getStartDate());
        context.setVariable("startDate", absenceRequest.getStartDate());
        context.setVariable("endDate", absenceRequest.getEndDate());
        context.setVariable("totalDays", absenceRequest.getTotalDays());
        context.setVariable("referenceNumber", absenceRequest.getReferenceNumber());

        context.setVariable("logoUrl","");
        // generate HTML content:
        String htmlContent = templateEngine.process("leave-rejected-manager.html", context);
        outlookEmailService.sendEmail(
                TO,
                htmlContent,
                "Demande d'absence rejetée par le service RH"
        );
        System.out.println("Leave approval email sent to:" + TO);
    }
}
