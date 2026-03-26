package com.saham.hr_system.modules.auth.service.implementation;

import com.saham.hr_system.modules.auth.repository.PasswordSetupTokenRepository;
import com.saham.hr_system.modules.auth.service.EmployeeAccountReactivation;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.modules.employees.service.implementation.EmployeeAccountReactivationEmailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;


@Service
public class EmployeeAccountReactivationImpl implements EmployeeAccountReactivation {

    private final EmployeePasswordSetupService employeePasswordSetupService;
    private final EmployeeAccountReactivationEmailSenderImpl employeeAccountReactivationEmailSender;
    private final EmployeeRepository employeeRepository;
    private final PasswordSetupTokenRepository passwordSetupTokenRepository;

    public EmployeeAccountReactivationImpl(EmployeePasswordSetupService employeePasswordSetupService, EmployeeAccountReactivationEmailSenderImpl employeeAccountReactivationEmailSender, EmployeeRepository employeeRepository, PasswordSetupTokenRepository passwordSetupTokenRepository) {
        this.employeePasswordSetupService = employeePasswordSetupService;
        this.employeeAccountReactivationEmailSender = employeeAccountReactivationEmailSender;
        this.employeeRepository = employeeRepository;
        this.passwordSetupTokenRepository = passwordSetupTokenRepository;
    }

    @Override
    public void reactivateEmployeeAccount(String email) {
        // Fetch the employee from DB
        Employee employee = employeeRepository
                .findByEmail(email).orElseThrow();
        // Remove the previous activation token
        passwordSetupTokenRepository.deleteByEmployee(employee);
        // Generate the token and link
        String link = employeePasswordSetupService.initiatePasswordSetup(email);

        CompletableFuture.runAsync(()->{
            try{
                // Send the email with the link
                employeeAccountReactivationEmailSender.sendReactivationEmail(email, link);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
