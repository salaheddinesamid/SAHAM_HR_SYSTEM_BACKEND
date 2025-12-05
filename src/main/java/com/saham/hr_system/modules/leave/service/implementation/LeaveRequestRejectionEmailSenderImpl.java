package com.saham.hr_system.modules.leave.service.implementation;

import com.saham.hr_system.modules.leave.model.LeaveRequest;
import com.saham.hr_system.modules.leave.service.LeaveRequestRejectionEmailSender;
import com.saham.hr_system.modules.leave.utils.LeaveTypeMapper;
import com.saham.hr_system.modules.leave.utils.LocalDateMapper;
import com.saham.hr_system.utils.OutlookEmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class LeaveRequestRejectionEmailSenderImpl implements LeaveRequestRejectionEmailSender {

    @Autowired
    private OutlookEmailService outlookEmailService;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private LeaveTypeMapper leaveTypeMapper;

    @Autowired
    private LocalDateMapper localDateMapper;

    @Override
    public void sendSubordinateRejectionEmailToEmployee(LeaveRequest leaveRequest) throws MessagingException {
        String to = leaveRequest.getEmployee().getEmail();

        //String to = "salaheddine.samid@medjoolstar.com";
        //helper.setTo("salaheddine.samid@medjoolstar.com"); // for testing purposes

        // Template variables
        Context context = new Context();
        context.setVariable("manager", leaveRequest.getEmployee().getManager().getFullName());
        context.setVariable("type", leaveTypeMapper.mapLeaveType(leaveRequest.getTypeOfLeave().toString()));
        context.setVariable("startDate", localDateMapper.mapToFrenchFormat(leaveRequest.getStartDate()));
        context.setVariable("endDate", localDateMapper.mapToFrenchFormat(leaveRequest.getEndDate()));
        context.setVariable("totalDays", leaveRequest.getTotalDays());

        context.setVariable("referenceNumber", leaveRequest.getReferenceNumber());
        context.setVariable("logoUrl", "https://yourpublicurl.com/logo.png");

        String htmlContent = templateEngine.process("leave-request-rejected-employee.html", context);
        outlookEmailService.sendEmail(
                to,
                htmlContent,
                "Votre demande de congé est rejeté par le manager"
        );
        System.out.println("Leave rejection email sent to: " + to);
    }
}
