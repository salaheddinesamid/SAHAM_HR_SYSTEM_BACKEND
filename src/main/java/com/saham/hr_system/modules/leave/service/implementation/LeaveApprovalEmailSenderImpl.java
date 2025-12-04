package com.saham.hr_system.modules.leave.service.implementation;

import com.saham.hr_system.modules.leave.model.LeaveRequest;
import com.saham.hr_system.modules.leave.service.LeaveApprovalEmailSender;
import com.saham.hr_system.utils.OutlookEmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class LeaveApprovalEmailSenderImpl implements LeaveApprovalEmailSender {

    @Autowired
    private OutlookEmailService outlookEmailService;

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public void sendHRApprovalEmailToManager(LeaveRequest leaveRequest) throws MessagingException {
        //String to = leaveRequest.getEmployee().getManager().getEmail();
        String to = "salaheddine.samid@medjoolstar.com";

        // Template engine variables:
        Context context = new Context();
        context.setVariable("managerName", leaveRequest.getEmployee().getManager().getFullName());
        context.setVariable("employeeName", leaveRequest.getEmployee().getManager().getFullName());
        context.setVariable("type", leaveRequest.getStartDate());
        context.setVariable("startDate", leaveRequest.getStartDate());
        context.setVariable("endDate", leaveRequest.getEndDate());
        context.setVariable("totalDays", leaveRequest.getTotalDays());
        context.setVariable("referenceNumber", leaveRequest.getReferenceNumber());

        context.setVariable("logoUrl","");
        // generate HTML content:
        String htmlContent = templateEngine.process("leave-approved-manager.html", context);
        outlookEmailService.sendEmail(
                to,
                htmlContent,
                "Demande de congé approuvée par le service RH"
        );
        System.out.println("Leave approval email sent to:" + leaveRequest.getEmployee().getEmail());
    }

    @Override
    public void sendHRApprovalEmailToEmployee(LeaveRequest leaveRequest) throws MessagingException{
        //String to = leaveRequest.getEmployee().getEmail();
        String to = "salaheddine.samid@medjoolstar.com";

        // Template engine variables:
        Context context = new Context();
        context.setVariable("employeeName", leaveRequest.getEmployee().getFullName());
        context.setVariable("type", leaveRequest.getTypeOfLeave().toString());
        context.setVariable("startDate", leaveRequest.getStartDate());
        context.setVariable("endDate", leaveRequest.getEndDate());
        context.setVariable("referenceNumber", leaveRequest.getReferenceNumber());
        context.setVariable("totalDays", leaveRequest.getTotalDays());
        context.setVariable("logoUrl","");
        // generate HTML content:
        String htmlContent = templateEngine.process("leave-approved-employee.html", context);
        outlookEmailService.sendEmail(
                to,
                htmlContent,
                "Votre demande de congé est approuvée par le service RH"
        );
        System.out.println("Leave approval email sent to:" + leaveRequest.getEmployee().getEmail());
    }
}
