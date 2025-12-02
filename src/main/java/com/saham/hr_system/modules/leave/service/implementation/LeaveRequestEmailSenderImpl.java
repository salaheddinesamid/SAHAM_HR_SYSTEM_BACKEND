package com.saham.hr_system.modules.leave.service.implementation;

import com.saham.hr_system.modules.leave.model.LeaveRequest;
import com.saham.hr_system.modules.leave.service.LeaveRequestEmailSender;
import com.saham.hr_system.modules.leave.utils.LeaveTypeMapper;
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

    @Autowired
    private LeaveTypeMapper leaveTypeMapper;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendEmployeeNotificationEmail(LeaveRequest leaveRequest) throws MessagingException {
        String employeeEmail = leaveRequest.getEmployee().getEmail();

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(from);
        //helper.setTo(employeeEmail);
        helper.setTo("salaheddine.samid@medjoolstar.com"); // For testing purposes
        helper.setSubject("Votre demande de congé a été enregistré");

        // Template variables
        Context context = new Context();
        String typeMapped = leaveTypeMapper.mapLeaveType(leaveRequest.getTypeOfLeave().toString());
        context.setVariable("type", typeMapped);
        context.setVariable("startDate", leaveRequest.getStartDate());
        context.setVariable("endDate", leaveRequest.getEndDate());
        context.setVariable("referenceNumber", leaveRequest.getReferenceNumber());
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
    public void sendManagerNotificationEmail(LeaveRequest leaveRequest) throws MessagingException {

        String managerEmail = leaveRequest.getEmployee().getManager().getEmail();
        assert managerEmail != null;
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(from);
        //helper.setTo(managerEmail);
        helper.setTo("salaheddine.samid@medjoolstar.com"); // For testing purposes
        helper.setSubject("Nouvelle demande de congé à valider");

        Context context = new Context();
        String typeMapped = leaveTypeMapper.mapLeaveType(leaveRequest.getTypeOfLeave().toString());
        context.setVariable("managerName", leaveRequest.getEmployee().getManager().getFullName());
        context.setVariable("employeeName", leaveRequest.getEmployee().getFullName());
        context.setVariable("requestLink", "");
        context.setVariable("type", typeMapped);
        context.setVariable("typeDetails", leaveRequest.getTypeDetails());
        context.setVariable("referenceNumber", leaveRequest.getReferenceNumber());
        context.setVariable("startDate", leaveRequest.getStartDate());
        context.setVariable("endDate", leaveRequest.getEndDate());

        String htmlContent = templateEngine.process("leave-requested-manager.html", context);
        helper.setText(htmlContent, true);

        javaMailSender.send(message);
        System.out.println("Leave request email sent to: " + managerEmail);
    }
}
