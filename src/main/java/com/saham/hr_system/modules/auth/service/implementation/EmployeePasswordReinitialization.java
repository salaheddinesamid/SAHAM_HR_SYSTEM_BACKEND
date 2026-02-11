package com.saham.hr_system.modules.auth.service.implementation;

import com.saham.hr_system.exception.PasswordResetTokenExpiredException;
import com.saham.hr_system.exception.UserNotFoundException;
import com.saham.hr_system.modules.auth.model.PasswordResetToken;
import com.saham.hr_system.modules.auth.repository.PasswordResetTokenRepository;
import com.saham.hr_system.modules.auth.service.PasswordReinitializationService;
import com.saham.hr_system.modules.auth.utils.ResetPasswordTokenGenerator;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class EmployeePasswordReinitialization implements PasswordReinitializationService {
    private final static Long TOKEN_VALIDITY_DURATION = 15L; // Token validity duration in minutes
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final ResetPasswordTokenGenerator tokenGenerator;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    public EmployeePasswordReinitialization(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder, ResetPasswordTokenGenerator tokenGenerator, PasswordResetTokenRepository passwordResetTokenRepository) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenGenerator = tokenGenerator;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    @Override
    public void initiatePasswordReset(String email) {
        // Check if the email exists in the system and belongs to an employee
        Employee employee  = employeeRepository.findByEmail(email)
                .orElseThrow(()-> new UserNotFoundException(email));
        // Otherwise, generate a password reset token and send it to the employee's email
        String resetToken = tokenGenerator.generateToken();
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setEmployee(employee);
        passwordResetToken.setToken(resetToken);
        passwordResetToken.setExpiryDate(LocalDateTime.now().plusMinutes(TOKEN_VALIDITY_DURATION));
        passwordResetTokenRepository.save(passwordResetToken);

        // notify the employee by email (this part is not implemented here, but you can use an email service to send the reset token to the employee's email address)
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        // Check if the token is valid and not expired
        PasswordResetToken t = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(()-> new RuntimeException("Invalid token"));
        Employee employee = t.getEmployee();
        if(t.isExpired()){
            throw new PasswordResetTokenExpiredException("");
        }
        // Check if the new password is not the same as the old one
        if(passwordEncoder.matches(employee.getPassword(), passwordEncoder.encode(newPassword))){
            throw new PasswordResetTokenExpiredException("");
        };
        // Otherwise, reset the employee's password and invalidate the token
        String hashedPassword = passwordEncoder.encode(newPassword);
        employee.setPassword(hashedPassword);
        employeeRepository.save(employee);
    }
}
