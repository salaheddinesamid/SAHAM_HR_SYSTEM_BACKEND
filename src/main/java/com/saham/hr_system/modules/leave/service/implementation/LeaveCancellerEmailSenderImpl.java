package com.saham.hr_system.modules.leave.service.implementation;

import com.saham.hr_system.modules.leave.model.Leave;
import com.saham.hr_system.modules.leave.service.LeaveCancelerEmailSender;
import com.saham.hr_system.modules.leave.utils.LeaveTypeMapper;
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
public class LeaveCancellerEmailSenderImpl implements LeaveCancelerEmailSender {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    private final LeaveTypeMapper leaveTypeMapper;

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    public LeaveCancellerEmailSenderImpl(LeaveTypeMapper leaveTypeMapper) {
        this.leaveTypeMapper = leaveTypeMapper;
    }


    @Override
    public void notifyEmployee(Leave leave) throws MessagingException {
        String employeeEmail = leave.getEmployee().getEmail();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(from);
        //helper.setTo(employeeEmail);
        helper.setTo("salaheddine.samid@medjoolstar.com"); // For testing purposes
        helper.setSubject("Votre congé a été annulée");

        // Template variables
        Context context = new Context();
        String typeMapped = leaveTypeMapper.mapLeaveType(leave.getLeaveType().toString());
        context.setVariable("type", typeMapped);
        context.setVariable("startDate", leave.getFromDate());
        context.setVariable("endDate", leave.getToDate());
        context.setVariable("referenceNumber", leave.getReferenceNumber());
        context.setVariable("totalDays", leave.getTotalDays());
        context.setVariable("logoUrl", "https://yourpublicurl.com/logo.png");

        String htmlContent = templateEngine.process("leave-cancelled-employee.html", context);
        helper.setText(htmlContent, true);

        mailSender.send(message);
        System.out.println("Leave cancellation email sent to employee: " + employeeEmail);

    }

    @Override
    public void notifyManager(Leave leave) throws MessagingException {
        String employeeEmail = leave.getEmployee().getEmail();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(from);
        //helper.setTo(employeeEmail);
        helper.setTo("salaheddine.samid@medjoolstar.com"); // For testing purposes
        helper.setSubject("Un congé de votre collaborateur/ice a été annulée");

        // Template variables
        Context context = new Context();
        String typeMapped = leaveTypeMapper.mapLeaveType(leave.getLeaveType().toString());
        context.setVariable("type", typeMapped);
        context.setVariable("employeeName", leave.getEmployee().getFirstName());
        context.setVariable("startDate", leave.getFromDate());
        context.setVariable("endDate", leave.getToDate());
        context.setVariable("referenceNumber", leave.getReferenceNumber());
        context.setVariable("totalDays", leave.getTotalDays());
        context.setVariable("logoUrl", "https://yourpublicurl.com/logo.png");

        String htmlContent = templateEngine.process("leave-cancelled-manager.html", context);
        helper.setText(htmlContent, true);

        mailSender.send(message);
        System.out.println("Leave cancellation email sent to manager: " + employeeEmail);

    }
}
