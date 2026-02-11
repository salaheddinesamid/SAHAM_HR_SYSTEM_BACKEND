package com.saham.hr_system.auth.unit;

import com.saham.hr_system.exception.UserNotFoundException;
import com.saham.hr_system.modules.auth.repository.PasswordResetTokenRepository;
import com.saham.hr_system.modules.auth.service.implementation.EmployeePasswordReinitialization;
import com.saham.hr_system.modules.auth.utils.ResetPasswordTokenGenerator;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class PasswordReinitializationUnitTest {

    @InjectMocks
    private EmployeePasswordReinitialization employeePasswordReinitialization;

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Mock
    private ResetPasswordTokenGenerator resetPasswordTokenGenerator;

    @Mock
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        employee = new Employee();
        employee.setEmail("test@saham.com");
    }

    @Test
    void testInitiatePasswordResetSuccess(){
        // Arrange:
        when(employeeRepository.findByEmail("test@saham.com")).thenReturn(Optional.of(employee));

        // Act and verify:
        employeePasswordReinitialization.initiatePasswordReset("test@saham.com");
        verify(passwordResetTokenRepository, times(1)).save(any());
    }

    @Test
    void testInitiatePasswordResetUserNotFound(){
        // Act and verify:
        assertThrows(UserNotFoundException.class, ()-> employeePasswordReinitialization.initiatePasswordReset("test@saham.com"));
    }

    @Test
    void testResetPasswordSuccess(){}

    @Test
    void testResetPasswordInvalidToken(){}

    @Test
    void testResetPasswordExpiredToken(){}
}
