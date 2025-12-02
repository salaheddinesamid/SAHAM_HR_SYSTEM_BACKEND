package com.saham.hr_system.modules.leave.service.implementation;

import com.saham.hr_system.modules.leave.model.LeaveRequest;
import com.saham.hr_system.modules.leave.service.LeaveRequestApprovalEmailSender;
import com.saham.hr_system.modules.leave.utils.HRFetcherUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

/**
 * This class implements EmailSender interface to send email to (Employee, and HR) when a leave request is approved by the manager.
 */
@Component
public class LeaveRequestApprovalEmailSenderImpl implements LeaveRequestApprovalEmailSender {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from;

    private final HRFetcherUtils hrFetcherUtils;

    @Autowired
    public LeaveRequestApprovalEmailSenderImpl(HRFetcherUtils hrFetcherUtils) {
        this.hrFetcherUtils = hrFetcherUtils;
    }


    @Override
    public void sendSubordinateApprovalEmailToEmployee(LeaveRequest leaveRequest) throws MessagingException {
        String employeeEmail = leaveRequest.getEmployee().getEmail();

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(from);
        helper.setTo(employeeEmail);
        //helper.setTo("salaheddine.samid@medjoolstar.com"); // for testing purposes
        helper.setSubject("Votre demande de congé est approuvée par le manager ✔");

        // Template variables
        Context context = new Context();
        context.setVariable("manager", leaveRequest.getEmployee().getManager().getFullName());
        context.setVariable("type", leaveRequest.getTypeOfLeave().toString());
        context.setVariable("startDate", leaveRequest.getStartDate());
        context.setVariable("endDate", leaveRequest.getEndDate());
        context.setVariable("referenceNumber", leaveRequest.getReferenceNumber());
        context.setVariable("logoUrl", "https://yourpublicurl.com/logo.png");

        String htmlContent = templateEngine.process("leave-request-approved-employee.html", context);
        helper.setText(htmlContent, true);

        javaMailSender.send(message);
        System.out.println("Leave request approval email sent to: " + employeeEmail);
    }

    @Override
    public void sendSubordinateApprovalEmailToHR(LeaveRequest leaveRequest) throws MessagingException{
        // fetch all HR emails:
        List<String> emails = hrFetcherUtils.fetchHREmail();
        emails.forEach(email -> {
            try{
                MimeMessage message = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
                helper.setFrom(from);
                helper.setTo(email);
                //helper.setTo("salaheddine.samid@medjoolstar.com"); // for testing purposes
                helper.setSubject("Nouvelle demande de congé à valider");

                // Template variables
                Context context = new Context();
                context.setVariable("managerName", leaveRequest.getEmployee().getManager().getFullName());
                context.setVariable("employeeName", leaveRequest.getEmployee().getFullName());
                context.setVariable("type", leaveRequest.getTypeOfLeave().toString());
                context.setVariable("startDate", leaveRequest.getStartDate());
                context.setVariable("endDate", leaveRequest.getEndDate());
                context.setVariable("referenceNumber", leaveRequest.getReferenceNumber());
                context.setVariable("logoUrl", "https://yourpublicurl.com/logo.png");

                String htmlContent = templateEngine.process("leave-request-approved-hr.html", context);
                helper.setText(htmlContent, true);
                javaMailSender.send(message);
                System.out.println("Leave request approval email sent to: " + email);
            }catch (MessagingException e){
                throw new RuntimeException(e);
            }

        });
    }
}
