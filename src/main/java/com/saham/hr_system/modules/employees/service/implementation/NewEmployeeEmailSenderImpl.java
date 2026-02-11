package com.saham.hr_system.modules.employees.service.implementation;

import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.service.NewEmployeeEmailSender;
import com.saham.hr_system.utils.OutlookEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class NewEmployeeEmailSenderImpl implements NewEmployeeEmailSender {

    private final TemplateEngine templateEngine;
    private final OutlookEmailService outlookEmailService;

    @Autowired
    public NewEmployeeEmailSenderImpl(TemplateEngine templateEngine, OutlookEmailService outlookEmailService) {
        this.templateEngine = templateEngine;
        this.outlookEmailService = outlookEmailService;
    }

    @Override
    public void sendWelcomeEmail(String employeeEmail, Employee employeeDetails, String link) {
        Context context = new Context();
        context.setVariable("link", link);
        context.setVariable("employeeName", employeeDetails.getFullName());
        context.setVariable("matriculation", employeeDetails.getEmployeeProfessionalDetails().getMatriculation());
        context.setVariable("joinDate", employeeDetails.getEmployeeProfessionalDetails().getJoinDate());
        context.setVariable("department", employeeDetails.getEmployeeProfessionalDetails().getDepartment());
        context.setVariable("managerName", employeeDetails.getEmployeeProfessionalDetails().getManager().getFullName());
        context.setVariable("cnssNumber", employeeDetails.getEmployeeSocialDetails().getCnssNumber());
        context.setVariable("cimrNumber", employeeDetails.getEmployeeSocialDetails().getCimrNumber());
        context.setVariable("insuranceNumber", employeeDetails.getEmployeeSocialDetails().getInsuranceNumber());

        String emailContent = templateEngine.process("welcome-email-template", context);

        outlookEmailService.sendEmail(employeeEmail, emailContent, "Welcome to SAHAM HR System!");
    }
}
