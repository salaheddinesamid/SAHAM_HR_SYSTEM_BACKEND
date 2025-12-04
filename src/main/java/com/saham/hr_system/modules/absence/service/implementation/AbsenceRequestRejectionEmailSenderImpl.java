package com.saham.hr_system.modules.absence.service.implementation;

import com.saham.hr_system.modules.absence.model.AbsenceRequest;
import com.saham.hr_system.modules.absence.service.AbsenceRequestRejectionEmailSender;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

@Component
public class AbsenceRequestRejectionEmailSenderImpl implements AbsenceRequestRejectionEmailSender {
    @Override
    public void notifyEmployee(AbsenceRequest absenceRequest) {
        //String to = leaveRequest.getEmployee().getEmail();

        String to = "salaheddine.samid@medjoolstar.com";
        //helper.setTo("salaheddine.samid@medjoolstar.com"); // for testing purposes

        // Template variables
        Context context = new Context();
        context.setVariable("manager", leaveRequest.getEmployee().getManager().getFullName());
        context.setVariable("type", leaveRequest.getTypeOfLeave().toString());
        context.setVariable("startDate", leaveRequest.getStartDate());
        context.setVariable("endDate", leaveRequest.getEndDate());
        context.setVariable("referenceNumber", leaveRequest.getReferenceNumber());
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
