package com.saham.hr_system.modules.leave.service.implementation;

import com.saham.hr_system.modules.leave.model.LeaveRequest;
import com.saham.hr_system.modules.leave.service.LeaveRequestEmailSender;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class LeaveRequestEmailSenderImpl implements LeaveRequestEmailSender {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendEmployeeNotificationEmail(LeaveRequest leaveRequest) throws MessagingException {
        String employeeEmail = leaveRequest.getEmployee().getEmail();

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(from);
        helper.setTo(employeeEmail);
        helper.setSubject("Votre demande de congé a été enregistré");

        // Template variables
        Context context = new Context();
        context.setVariable("type", leaveRequest.getTypeOfLeave().toString());
        context.setVariable("startDate", leaveRequest.getStartDate());
        context.setVariable("endDate", leaveRequest.getEndDate());

        context.setVariable("logoUrl", "https://yourpublicurl.com/logo.png");

        String htmlContent = templateEngine.process("leave-requested-employee.html", context);
        helper.setText(htmlContent, true);

        javaMailSender.send(message);
        System.out.println("Leave request email sent to: " + employeeEmail);
    }

    /**
     * Email notification for the manager after submission of a leave request:
     */
    @Override
    public void sendManagerNotificationEmail(LeaveRequest leaveRequest, String managerEmail) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(from);
        helper.setTo(managerEmail);
        helper.setSubject("Nouvelle demande de congé à valider");

        Context context = new Context();
        context.setVariable("employeeName", leaveRequest.getEmployee().getFullName());
        context.setVariable("type", leaveRequest.getTypeOfLeave().toString());
        context.setVariable("startDate", leaveRequest.getStartDate());
        context.setVariable("endDate", leaveRequest.getEndDate());

        String htmlContent = templateEngine.process("leave-requested-manager.html", context);
        helper.setText(htmlContent, true);

        javaMailSender.send(message);
        System.out.println("Leave request email sent to: " + managerEmail);
    }
}
