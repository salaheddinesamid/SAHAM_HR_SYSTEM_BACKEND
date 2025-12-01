package com.saham.hr_system.modules.leave.service.implementation;

import com.saham.hr_system.modules.leave.model.LeaveRequest;
import com.saham.hr_system.modules.leave.service.LeaveRequestRejectionEmailSender;
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
public class LeaveRequestRejectionEmailSenderImpl implements LeaveRequestRejectionEmailSender {

    @Autowired
    private JavaMailSender javamailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendSubordinateRejectionEmailToEmployee(LeaveRequest leaveRequest) throws MessagingException {
        String employeeEmail = leaveRequest.getEmployee().getEmail();

        MimeMessage message = javamailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(from);
        helper.setTo("salaheddine.samid@medjoolstar.com"); // for testing purposes
        helper.setSubject("Votre demande de congé est rejeté par le manager");

        // Template variables
        Context context = new Context();
        context.setVariable("manager", leaveRequest.getEmployee().getManager().getFullName());
        context.setVariable("type", leaveRequest.getTypeOfLeave().toString());
        context.setVariable("startDate", leaveRequest.getStartDate());
        context.setVariable("endDate", leaveRequest.getEndDate());

        context.setVariable("logoUrl", "https://yourpublicurl.com/logo.png");

        String htmlContent = templateEngine.process("leave-request-rejected-employee.html", context);
        helper.setText(htmlContent, true);

        javamailSender.send(message);
        System.out.println("Leave rejection email sent to: " + employeeEmail);
    }
}
