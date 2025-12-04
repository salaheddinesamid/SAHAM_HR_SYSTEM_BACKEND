package com.saham.hr_system.modules.leave.service.implementation;

import com.saham.hr_system.modules.leave.model.LeaveRequest;
import com.saham.hr_system.modules.leave.service.LeaveRequestApprovalEmailSender;
import com.saham.hr_system.modules.leave.utils.HRFetcherUtils;
import com.saham.hr_system.utils.OutlookEmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
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
    private OutlookEmailService outlookEmailService;

    @Autowired
    private TemplateEngine templateEngine;

    private final HRFetcherUtils hrFetcherUtils;

    @Autowired
    public LeaveRequestApprovalEmailSenderImpl(HRFetcherUtils hrFetcherUtils) {
        this.hrFetcherUtils = hrFetcherUtils;
    }


    @Override
    public void sendSubordinateApprovalEmailToEmployee(LeaveRequest leaveRequest) throws MessagingException {
        //String to = leaveRequest.getEmployee().getEmail();
        String to = "salaheddine.samid@saham.com";

        // Template variables
        Context context = new Context();
        context.setVariable("manager", leaveRequest.getEmployee().getManager().getFullName());
        context.setVariable("type", leaveRequest.getTypeOfLeave().toString());
        context.setVariable("startDate", leaveRequest.getStartDate());
        context.setVariable("endDate", leaveRequest.getEndDate());
        context.setVariable("referenceNumber", leaveRequest.getReferenceNumber());
        context.setVariable("logoUrl", "https://yourpublicurl.com/logo.png");

        String htmlContent = templateEngine.process("leave-request-approved-employee.html", context);
        outlookEmailService.sendEmail(
                to,
                htmlContent,
                "Votre demande de congé a été approuvée par le manager"
        );
        System.out.println("Leave request approval email sent to: " + to);
    }

    @Override
    public void sendSubordinateApprovalEmailToHR(LeaveRequest leaveRequest) throws MessagingException{
        // fetch all HR emails:
        List<String> emails = hrFetcherUtils.fetchHREmail();
        emails.forEach(email -> {
            //String to = email;
            String to = "salaheddine.samid@medjoolstar.com"; // for testing purposes

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
            outlookEmailService.sendEmail(
                    to,
                    htmlContent,
                    "Nouvelle demande de congé à valider"
            );
            System.out.println("Leave request approval email sent to: " + email);

        });
    }
}
