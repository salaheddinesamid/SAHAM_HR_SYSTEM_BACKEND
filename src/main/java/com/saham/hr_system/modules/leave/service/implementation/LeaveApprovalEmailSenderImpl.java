package com.saham.hr_system.modules.leave.service.implementation;

import com.saham.hr_system.modules.leave.model.LeaveRequest;
import com.saham.hr_system.modules.leave.service.LeaveApprovalEmailSender;
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
public class LeaveApprovalEmailSenderImpl implements LeaveApprovalEmailSender {

    @Value("${spring.mail.username")
    private String from;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public void sendHRApprovalEmailToManager(LeaveRequest leaveRequest, String email) throws MessagingException {

    }

    @Override
    public void sendHRApprovalEmailToEmployee(LeaveRequest leaveRequest) throws MessagingException{
        String employeeEmail = leaveRequest.getEmployee().getEmail();
        // Create mime message:
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message); // message helper:

        // set information:
        helper.setFrom(from);
        helper.setTo(employeeEmail);
        helper.setSubject("Votre congé a été accepter");

        // Template engine variables:
        Context context = new Context();
        context.setVariable("employeeName", leaveRequest.getEmployee().getManager().getFullName());
        context.setVariable("type", leaveRequest.getTypeOfLeave().toString());
        context.setVariable("startDate", leaveRequest.getStartDate());
        context.setVariable("endDate", leaveRequest.getEndDate());

        context.setVariable("logoUrl","");
        // generate HTML content:
        String htmlContent = templateEngine.process("leave-approved-employee.html", context);
        helper.setText(htmlContent, true);
        mailSender.send(message);
        System.out.println("Leave approval email sent to:" + leaveRequest.getEmployee().getEmail());
    }
}
