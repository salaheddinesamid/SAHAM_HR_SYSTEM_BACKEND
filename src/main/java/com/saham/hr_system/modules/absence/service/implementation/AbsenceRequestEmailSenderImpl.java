package com.saham.hr_system.modules.absence.service.implementation;

import com.saham.hr_system.modules.absence.model.AbsenceRequest;
import com.saham.hr_system.modules.absence.service.AbsenceRequestEmailSender;
import com.saham.hr_system.modules.absence.utils.AbsenceTypeMapper;
import com.saham.hr_system.utils.OutlookEmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class AbsenceRequestEmailSenderImpl implements AbsenceRequestEmailSender {

    @Autowired
    private OutlookEmailService outlookEmailService;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private AbsenceTypeMapper absenceTypeMapper;

    private final static String TO = "salaheddine.samid@medjoolstar.com";

    @Override
    public void notifyEmployee(AbsenceRequest absenceRequest) throws MessagingException {
        //String TO = absenceRequest.getEmployee().getEmail();

        // Template variables
        Context context = new Context();
        String typeMapped = absenceTypeMapper.mapToReadableFormat(absenceRequest.getType().toString());
        context.setVariable("type", typeMapped);
        context.setVariable("startDate", absenceRequest.getStartDate());
        context.setVariable("endDate", absenceRequest.getEndDate());
        context.setVariable("referenceNumber", absenceRequest.getReferenceNumber());
        context.setVariable("totalDays", absenceRequest.getTotalDays());
        context.setVariable("logoUrl", "https://yourpublicurl.com/logo.png");

        String htmlContent = templateEngine.process("absence-requested-employee.html", context);

        outlookEmailService.sendEmail(
                TO,
                htmlContent,
                "Votre demande d'absence a été soumise ✔");
        System.out.println("Absence request email sent to: " + TO);
    }

    @Override
    public void notifyManager(AbsenceRequest absenceRequest) throws MessagingException {
        //String TO = absenceRequest.getEmployee().getEmail();

        // Template variables
        Context context = new Context();
        String typeMapped = absenceTypeMapper.mapToReadableFormat(absenceRequest.getType().toString());
        context.setVariable("type", typeMapped);
        context.setVariable("startDate", absenceRequest.getStartDate());
        context.setVariable("managerName", absenceRequest.getEmployee().getManager().getFullName());
        context.setVariable("employeeName", absenceRequest.getEmployee().getFullName());
        context.setVariable("endDate", absenceRequest.getEndDate());
        context.setVariable("referenceNumber", absenceRequest.getReferenceNumber());
        context.setVariable("totalDays", absenceRequest.getTotalDays());
        context.setVariable("logoUrl", "https://yourpublicurl.com/logo.png");

        String htmlContent = templateEngine.process(
                "absence-requested-manager.html",
                context);

        outlookEmailService.sendEmail(
                TO,
                htmlContent,
                "Votre demande d'absence a été soumise ✔");
        System.out.println("Absence request email sent to: " + TO);
    }
}
