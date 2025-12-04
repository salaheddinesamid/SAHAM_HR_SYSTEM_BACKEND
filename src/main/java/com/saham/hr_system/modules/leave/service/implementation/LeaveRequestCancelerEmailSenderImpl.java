package com.saham.hr_system.modules.leave.service.implementation;

import com.saham.hr_system.modules.leave.model.LeaveRequest;
import com.saham.hr_system.modules.leave.service.LeaveRequestCancelerEmailSender;
import com.saham.hr_system.modules.leave.utils.LeaveTypeMapper;
import com.saham.hr_system.utils.OutlookEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class LeaveRequestCancelerEmailSenderImpl implements LeaveRequestCancelerEmailSender {

    @Autowired
    private OutlookEmailService outlookEmailService;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private LeaveTypeMapper leaveTypeMapper;

    @Override
    public void notifyEmployee(LeaveRequest leaveRequest) throws MessagingException {
        //String to = leaveRequest.getEmployee().getEmail();
        String to = "salaheddine.samid@medjoolstar.com";

        // Template variables
        Context context = new Context();
        String typeMapped = leaveTypeMapper.mapLeaveType(leaveRequest.getTypeOfLeave().toString());
        context.setVariable("type", typeMapped);
        context.setVariable("startDate", leaveRequest.getStartDate());
        context.setVariable("endDate", leaveRequest.getEndDate());
        context.setVariable("referenceNumber", leaveRequest.getReferenceNumber());
        context.setVariable("totalDays", leaveRequest.getTotalDays());
        context.setVariable("logoUrl", "https://yourpublicurl.com/logo.png");

        String htmlContent = templateEngine.process("leave-request-cancelled-employee.html", context);
        outlookEmailService.sendEmail(
                to,
                htmlContent,
                "Votre demande de congé a été annulée"
        );
        System.out.println("Leave request cancellation email sent to employee: " + to);
    }

    @Override
    public void notifyManager(LeaveRequest leaveRequest) throws MessagingException {
        // String to = leaveRequest.getEmployee().getEmail();
        String to = "salaheddine.samid@medjoolstar.com";


        // Template variables
        Context context = new Context();
        String typeMapped = leaveTypeMapper.mapLeaveType(leaveRequest.getTypeOfLeave().toString());
        context.setVariable("type", typeMapped);
        context.setVariable("startDate", leaveRequest.getStartDate());
        context.setVariable("endDate", leaveRequest.getEndDate());
        context.setVariable("referenceNumber", leaveRequest.getReferenceNumber());
        context.setVariable("logoUrl", "https://yourpublicurl.com/logo.png");

        String htmlContent = templateEngine.process("leave-request-cancelled-employee.html", context);

        outlookEmailService.sendEmail(
                to,
                htmlContent,
                "Annulation de la demande de congé d'un collaborateur/ice"
        );
        System.out.println("Leave request cancellation email sent to manager: " + to);
    }
}
