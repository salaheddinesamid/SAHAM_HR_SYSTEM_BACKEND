package com.saham.hr_system.modules.leave.service.implementation;

import com.saham.hr_system.modules.leave.model.LeaveRequest;
import com.saham.hr_system.modules.leave.service.LeaveRequestEmailSender;
import com.saham.hr_system.modules.leave.utils.LeaveTypeMapper;
import com.saham.hr_system.modules.leave.utils.LocalDateMapper;
import com.saham.hr_system.utils.OutlookEmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class LeaveRequestEmailSenderImpl implements LeaveRequestEmailSender {

    @Autowired
    private OutlookEmailService outlookEmailService;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private LeaveTypeMapper leaveTypeMapper;

    @Autowired
    private LocalDateMapper localDateMapper;


    @Override
    public void sendEmployeeNotificationEmail(LeaveRequest leaveRequest) throws MessagingException {
        //String to = leaveRequest.getEmployee().getEmail();
        String to = "salaheddine.samid@medjoolstar.com";
        // Template variables
        Context context = new Context();
        String typeMapped = leaveTypeMapper.mapLeaveType(leaveRequest.getTypeOfLeave().toString());
        context.setVariable("type", typeMapped);
        context.setVariable("startDate", localDateMapper.mapToFrenchFormat(leaveRequest.getStartDate()));
        context.setVariable("endDate", localDateMapper.mapToFrenchFormat(leaveRequest.getEndDate()));
        context.setVariable("referenceNumber", leaveRequest.getReferenceNumber());
        context.setVariable("totalDays", leaveRequest.getTotalDays());
        context.setVariable("logoUrl", "https://yourpublicurl.com/logo.png");

        String htmlContent = templateEngine.process("leave-requested-employee.html", context);

        outlookEmailService.sendEmail(
                to,
                htmlContent,
                "Votre demande de congé a été soumise avec succès"
        );
        System.out.println("Leave request email sent to: " + to);
    }

    /**
     * Email notification for the manager after submission of a leave request:
     */
    @Override
    public void sendManagerNotificationEmail(LeaveRequest leaveRequest) throws MessagingException {

        //String to = leaveRequest.getEmployee().getManager().getEmail();
        String to = "salaheddine.samid@medjoolstar.com";

        Context context = new Context();
        String typeMapped = leaveTypeMapper.mapLeaveType(leaveRequest.getTypeOfLeave().toString());
        context.setVariable("managerName", leaveRequest.getEmployee().getManager().getFullName());
        context.setVariable("employeeName", leaveRequest.getEmployee().getFullName());
        context.setVariable("requestLink", "");
        context.setVariable("type", typeMapped);
        context.setVariable("typeDetails", leaveRequest.getTypeDetails());
        context.setVariable("referenceNumber", leaveRequest.getReferenceNumber());
        context.setVariable("startDate", localDateMapper.mapToFrenchFormat(leaveRequest.getStartDate()));
        context.setVariable("totalDays", leaveRequest.getTotalDays());
        context.setVariable("endDate", localDateMapper.mapToFrenchFormat(leaveRequest.getEndDate()));

        String htmlContent = templateEngine.process("leave-requested-manager.html", context);
        outlookEmailService.sendEmail(
                to,
                htmlContent,
                "Nouvelle demande de congé à approuver"
        );
        System.out.println("Leave request email sent to: " + to);
    }
}
