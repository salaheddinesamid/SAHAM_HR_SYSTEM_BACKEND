package com.saham.hr_system.modules.leave.service.implementation;

import com.saham.hr_system.modules.leave.model.Leave;
import com.saham.hr_system.modules.leave.service.LeaveCancelerEmailSender;
import com.saham.hr_system.modules.leave.utils.LeaveTypeMapper;
import com.saham.hr_system.utils.OutlookEmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class LeaveCancellerEmailSenderImpl implements LeaveCancelerEmailSender {

    @Autowired
    private OutlookEmailService outlookEmailService;

    @Autowired
    private TemplateEngine templateEngine;

    private final LeaveTypeMapper leaveTypeMapper;

    @Autowired
    public LeaveCancellerEmailSenderImpl(LeaveTypeMapper leaveTypeMapper) {
        this.leaveTypeMapper = leaveTypeMapper;
    }


    @Override
    public void notifyEmployee(Leave leave) throws MessagingException {
        //String to = leave.getEmployee().getEmail();
        String to = "salaheddine.samid@medjoolstar.com";

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
        outlookEmailService
                .sendEmail(
                        to,
                        htmlContent,
                        "Votre congé a été annulé"
                );
        System.out.println("Leave cancellation email sent to employee: " + to);

    }

    @Override
    public void notifyManager(Leave leave) throws MessagingException {
        //String to = leave.getEmployee().getEmail();
        String to = "salaheddine.samid@medjoolstar.com";

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
        outlookEmailService.sendEmail(
                to,
                htmlContent,
                "Un congé de votre collaborateur/ice a été annulée"
        );
        System.out.println("Leave cancellation email sent to manager: " + to);

    }
}
