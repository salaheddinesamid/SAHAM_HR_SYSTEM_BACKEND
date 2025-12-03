package com.saham.hr_system.modules.leave.service.implementation;

import com.saham.hr_system.modules.leave.model.LeaveRequest;
import com.saham.hr_system.modules.leave.service.LeaveRequestCancelerEmailSender;
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
public class LeaveRequestCancelerEmailSenderImpl implements LeaveRequestCancelerEmailSender {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private LeaveTypeMapper leaveTypeMapper;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void notifyEmployee(LeaveRequest leaveRequest) throws MessagingException {
        String employeeEmail = leaveRequest.getEmployee().getEmail();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(from);
        //helper.setTo(employeeEmail);
        helper.setTo("salaheddine.samid@medjoolstar.com"); // For testing purposes
        helper.setSubject("Votre demande de congé a été annulée");

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
        helper.setText(htmlContent, true);

        mailSender.send(message);
        System.out.println("Leave request cancellation email sent to employee: " + employeeEmail);
    }

    @Override
    public void notifyManager(LeaveRequest leaveRequest) throws MessagingException {
        String managerEmail = leaveRequest.getEmployee().getEmail();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(from);
        //helper.setTo(managerEmail);
        helper.setTo("salaheddine.samid@medjoolstar.com"); // For testing purposes
        helper.setSubject("Une demande de congé de votre collaborateur/ice a été annulée");

        // Template variables
        Context context = new Context();
        String typeMapped = leaveTypeMapper.mapLeaveType(leaveRequest.getTypeOfLeave().toString());
        context.setVariable("type", typeMapped);
        context.setVariable("startDate", leaveRequest.getStartDate());
        context.setVariable("endDate", leaveRequest.getEndDate());
        context.setVariable("referenceNumber", leaveRequest.getReferenceNumber());
        context.setVariable("logoUrl", "https://yourpublicurl.com/logo.png");

        String htmlContent = templateEngine.process("leave-request-cancelled-employee.html", context);
        helper.setText(htmlContent, true);

        mailSender.send(message);
        System.out.println("Leave request cancellation email sent to manager: " + managerEmail);
    }
}
