package com.saham.hr_system.modules.absence.service.implementation;

import com.saham.hr_system.modules.absence.model.AbsenceRequest;
import com.saham.hr_system.modules.absence.service.AbsenceRequestEmailSender;
import com.saham.hr_system.modules.absence.utils.AbsenceTypeMapper;
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
public class AbsenceRequestEmailSenderImpl implements AbsenceRequestEmailSender {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private AbsenceTypeMapper absenceTypeMapper;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void notifyEmployee(AbsenceRequest absenceRequest) throws MessagingException {
        String employeeEmail = absenceRequest.getEmployee().getEmail();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(from);
        //helper.setTo(employeeEmail);
        helper.setTo("salaheddine.samid@medjoolstar.com"); // For testing purposes
        helper.setSubject("Votre demande d'absence a été enregistré");

        // Template variables
        Context context = new Context();
        String typeMapped = absenceTypeMapper.mapToReadableFormat(absenceRequest.getType().toString());
        context.setVariable("type", typeMapped);
        context.setVariable("startDate", absenceRequest.getStartDate());
        context.setVariable("endDate", absenceRequest.getEndDate());
        context.setVariable("referenceNumber", absenceRequest.getReferenceNumber());
        context.setVariable("totalDays", absenceRequest.getTotalDays());
        context.setVariable("logoUrl", "https://yourpublicurl.com/logo.png");

        String htmlContent = templateEngine.process("absence-requested-employee.html", context);
        helper.setText(htmlContent, true);

        mailSender.send(message);
        System.out.println("Absence request email sent to: " + employeeEmail);
    }

    @Override
    public void notifyManager(AbsenceRequest absenceRequest) throws MessagingException {
        String managerEmail = absenceRequest.getEmployee().getManager().getEmail();
        assert managerEmail != null;
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(from);
        //helper.setTo(managerEmail);
        helper.setTo("salaheddine.samid@medjoolstar.com"); // For testing purposes
        helper.setSubject("Nouvelle demande d'absence à valider");

        Context context = new Context();
        String typeMapped = absenceTypeMapper.mapToReadableFormat(absenceRequest.getType().toString());
        context.setVariable("managerName", absenceRequest.getEmployee().getManager().getFullName());
        context.setVariable("employeeName", absenceRequest.getEmployee().getFullName());
        context.setVariable("requestLink", "");
        context.setVariable("type", typeMapped);
        context.setVariable("typeDetails", absenceRequest.getType());
        context.setVariable("referenceNumber", absenceRequest.getReferenceNumber());
        context.setVariable("startDate", absenceRequest.getStartDate());
        context.setVariable("endDate", absenceRequest.getEndDate());
        context.setVariable("totalDays", absenceRequest.getTotalDays());

        String htmlContent = templateEngine.process("absence-requested-manager.html", context);
        helper.setText(htmlContent, true);

        mailSender.send(message);
        System.out.println("Leave request email sent to: " + managerEmail);
    }
}
