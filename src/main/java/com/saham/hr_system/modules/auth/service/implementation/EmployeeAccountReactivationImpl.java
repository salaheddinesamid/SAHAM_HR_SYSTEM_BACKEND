package com.saham.hr_system.modules.auth.service.implementation;

import com.saham.hr_system.modules.auth.model.PasswordSetupToken;
import com.saham.hr_system.modules.auth.repository.PasswordSetupTokenRepository;
import com.saham.hr_system.modules.auth.service.EmployeeAccountReactivation;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.modules.employees.service.implementation.EmployeeAccountReactivationEmailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
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
    @Transactional
    public void reactivateEmployeeAccount(String email) {
        // Fetch the employee from DB
        Employee employee = employeeRepository
                .findByEmail(email).orElseThrow();
        // Remove the previous activation token

        // Remplace the old token if exists, to avoid multiple tokens for the same employee
        PasswordSetupToken employeeToken =
                passwordSetupTokenRepository.findByEmployee(employee).orElse(null);
        String newToken = UUID.randomUUID().toString();
        if(employeeToken != null){
            employeeToken.setToken(newToken);
            employeeToken.setUsed(false);
            employeeToken.setExpiryDate(LocalDateTime.now().plusMinutes(60L * 24 * 7));
        }
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
