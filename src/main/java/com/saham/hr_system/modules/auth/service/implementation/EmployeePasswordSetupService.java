package com.saham.hr_system.modules.auth.service.implementation;

import com.saham.hr_system.exception.UserNotFoundException;
import com.saham.hr_system.modules.auth.model.PasswordSetupToken;
import com.saham.hr_system.modules.auth.repository.PasswordSetupTokenRepository;
import com.saham.hr_system.modules.auth.service.PasswordSetupService;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class EmployeePasswordSetupService implements PasswordSetupService {

    @Value("${frontend.url}")
    private String PASSWORD_RESET_URL;
    private final static Long PASSWORD_RESET_TOKEN_EXPIRATION_MINUTES = 60L; // Token valid for 60 minutes
    private final EmployeeRepository employeeRepository;
    private final PasswordSetupTokenRepository passwordSetupTokenRepository;
    @Autowired
    public EmployeePasswordSetupService(EmployeeRepository employeeRepository, PasswordSetupTokenRepository passwordSetupTokenRepository) {
        this.employeeRepository = employeeRepository;
        this.passwordSetupTokenRepository = passwordSetupTokenRepository;
    }

    @Override
    public String initiatePasswordSetup(String email) {
        // Check if the email exists in the system (this would typically involve querying the database)
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(()-> new UserNotFoundException(email));
        String token =
                UUID.randomUUID().toString();
        PasswordSetupToken passwordSetupToken = new PasswordSetupToken();
        passwordSetupToken.setToken(token);
        passwordSetupToken.setEmployee(employee);
        passwordSetupToken.setExpiryDate(LocalDateTime.now().plusMinutes(PASSWORD_RESET_TOKEN_EXPIRATION_MINUTES));

        passwordSetupTokenRepository.save(passwordSetupToken);
        // return the token to be sent to the employee's email:
        return String.format("%s/setup-password?token=%s", PASSWORD_RESET_URL, token);
    }

    @Override
    public void setupPassword(String email, String token, String newPassword) {
        // Fetch the employee from db:
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(()-> new UserNotFoundException(email));

        // Fetch the token from db:
        PasswordSetupToken passwordSetupToken = passwordSetupTokenRepository.findByToken(token)
                .orElseThrow(()-> new RuntimeException("Password setup token not found for email: " + email));

        // Check if the token is expired:
        if (passwordSetupToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Password setup token has expired for email: " + email);
        }
        // Check if the token has been used:
        if(passwordSetupToken.isUsed()) {
            throw new RuntimeException("Password setup token has already been used for email: " + email);
        }
        passwordSetupToken.setUsed(true);

        // Update the employee's password (you would typically hash the password before saving it)
        employee.setPassword(newPassword); // In a real application, make sure to hash the password!
        employeeRepository.save(employee);

        // Invalidate the token after successful password setup
        passwordSetupTokenRepository.delete(passwordSetupToken);
    }
}
