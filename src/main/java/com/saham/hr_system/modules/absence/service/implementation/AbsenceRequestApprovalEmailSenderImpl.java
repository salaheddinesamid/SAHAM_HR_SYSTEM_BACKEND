package com.saham.hr_system.modules.absence.service.implementation;

import com.saham.hr_system.modules.absence.model.AbsenceRequest;
import com.saham.hr_system.modules.absence.service.AbsenceRequestApprovalEmailSender;
import com.saham.hr_system.modules.absence.utils.AbsenceTypeMapper;
import com.saham.hr_system.modules.leave.utils.HRFetcherUtils;
import com.saham.hr_system.modules.leave.utils.LocalDateMapper;
import com.saham.hr_system.utils.OutlookEmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

@Component
public class AbsenceRequestApprovalEmailSenderImpl implements AbsenceRequestApprovalEmailSender {

    @Autowired
    private OutlookEmailService outlookEmailService;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private HRFetcherUtils hrFetcherUtils;

    @Autowired
    private AbsenceTypeMapper absenceTypeMapper;

    @Autowired
    private LocalDateMapper localDateMapper;

    private final static String TO = "salaheddine.samid@medjoolstar.com";
    @Override
    public void notifyEmployee(AbsenceRequest absenceRequest) throws MessagingException {
        //String TO = leaveRequest.getEmployee().getEmail();

        // Template variables
        Context context = new Context();
        context.setVariable("manager", absenceRequest.getEmployee().getManager().getFullName());
        context.setVariable("type", absenceTypeMapper.mapToReadableFormat(absenceRequest.getType().toString()));
        context.setVariable("startDate", localDateMapper.mapToFrenchFormat(absenceRequest.getStartDate()));
        context.setVariable("endDate", localDateMapper.mapToFrenchFormat(absenceRequest.getEndDate()));
        context.setVariable("referenceNumber", absenceRequest.getReferenceNumber());
        context.setVariable("totalDays", absenceRequest.getTotalDays());
        context.setVariable("logoUrl", "https://yourpublicurl.com/logo.png");

        String htmlContent = templateEngine.process("absence-request-approved-employee.html", context);
        outlookEmailService.sendEmail(
                TO,
                htmlContent,
                "Votre demande d'absence a été approuvée par le manager"
        );
        System.out.println("Absence request approval email sent to: " + TO);
    }

    @Override
    public void notifyHR(AbsenceRequest absenceRequest) throws MessagingException {
        // fetch all HR emails:
        List<String> emails = hrFetcherUtils.fetchHREmail();
        emails.forEach(email -> {
            //String TO = email;

            // Template variables
            Context context = new Context();
            context.setVariable("managerName", absenceRequest.getEmployee().getManager().getFullName());
            context.setVariable("employeeName", absenceRequest.getEmployee().getFullName());
            context.setVariable("type", absenceTypeMapper.mapToReadableFormat(absenceRequest.getType().toString()));
            context.setVariable("startDate", localDateMapper.mapToFrenchFormat(absenceRequest.getStartDate()));
            context.setVariable("endDate", localDateMapper.mapToFrenchFormat(absenceRequest.getEndDate()));
            context.setVariable("referenceNumber", absenceRequest.getReferenceNumber());
            context.setVariable("totalDays", absenceRequest.getTotalDays());
            context.setVariable("logoUrl", "https://yourpublicurl.com/logo.png");

            String htmlContent = templateEngine.process("absence-request-approved-hr.html", context);
            outlookEmailService.sendEmail(
                    TO,
                    htmlContent,
                    "Nouvelle demande d'absence à valider"
            );
            System.out.println("Absence request approval email sent to: " + TO);

        });
    }
}
