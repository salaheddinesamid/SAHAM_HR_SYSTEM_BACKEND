package com.saham.hr_system.modules.leave.service.implementation;

import com.saham.hr_system.modules.leave.model.LeaveRequest;
import com.saham.hr_system.modules.leave.service.LeaveRequestEmailSender;
import jakarta.mail.Message;
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
    private JavaMailSender javaEmailSender;


    @Value("${spring.mail.username}")
    private String from;

    @Override
    public String generateEmployeeContent(LeaveRequest leaveRequest) {
        Context context = new Context();
        return null;
    }

    @Override
    public String generateManagerContent(LeaveRequest leaveRequest) {
        return "";
    }

    /**
     * This function will email the employee and manager.
     * The employee will receive a message of submission.
     * The manager will receive a message of notification.
     */
    @Override
    public void send() throws MessagingException {
        String employeeEmail = "salaheddine.samid@medjoolstar.com";
        String managerEmail = "";
        MimeMessage message = javaEmailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, "UTF-8");
        sendToEmployee(from, employeeEmail, "Leave Request Submitted", "Your leave request has been submitted.");
        mimeMessageHelper.setTo(employeeEmail);
        mimeMessageHelper.setFrom(from);
        mimeMessageHelper.setSubject("Leave Request Submission");

        javaEmailSender.send(message);
    }

    /**
     * Send email to employee
     */
    private void sendToEmployee(String from, String to, String subject, String body) {

    }

    /**
     *  Send email to manager
     */
    private void sendToManager(String from, String to, String subject, String body) {

    }

    private void sendHtml(String from, String to, String subject, String body) {

    }
}
