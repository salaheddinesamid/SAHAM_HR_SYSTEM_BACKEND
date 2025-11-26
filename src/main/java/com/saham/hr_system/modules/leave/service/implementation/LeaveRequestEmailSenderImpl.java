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

    /**
     * Email de confirmation pour l'employé après approbation
     */
    @Override
    public void sendLeaveApprovalEmail(LeaveRequest leaveRequest) throws MessagingException {
        String employeeEmail = leaveRequest.getEmployee().getEmail();

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(from);
        helper.setTo("boucheriezenata@gmail.com");
        helper.setSubject("Votre demande de congé est approuvée ✔");

        // Template variables
        Context context = new Context();
        context.setVariable("type", leaveRequest.getTypeOfLeave().toString());
        context.setVariable("startDate", leaveRequest.getStartDate());
        context.setVariable("endDate", leaveRequest.getEndDate());
        context.setVariable("managerName", leaveRequest.getEmployee().getManager().getFullName());

        // 🔥 Use your public URL for logo or CID if local
        context.setVariable("logoUrl", "https://yourpublicurl.com/logo.png");

        String htmlContent = templateEngine.process("leave-requested-employee.html", context);
        helper.setText(htmlContent, true);

        javaMailSender.send(message);
        System.out.println("Leave approval email sent to: " + employeeEmail);
    }

    /**
     * Email d'information au manager lors de la soumission
     */
    @Override
    public void sendManagerNotificationEmail(LeaveRequest leaveRequest, String managerEmail) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(from);
        helper.setTo("boucheriezenata@gmail.com");
        helper.setSubject("Nouvelle demande de congé à valider");

        Context context = new Context();
        context.setVariable("employeeName", leaveRequest.getEmployee().getFullName());
        context.setVariable("type", leaveRequest.getTypeOfLeave().toString());
        context.setVariable("startDate", leaveRequest.getStartDate());
        context.setVariable("endDate", leaveRequest.getEndDate());

        String htmlContent = templateEngine.process("leave-requested-employee.html", context);
        helper.setText(htmlContent, true);

        javaMailSender.send(message);
    }
}
