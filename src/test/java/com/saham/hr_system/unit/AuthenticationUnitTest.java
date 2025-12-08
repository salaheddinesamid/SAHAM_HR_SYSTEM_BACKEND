package com.saham.hr_system.unit;

import com.saham.hr_system.jwt.JwtUtilities;
import com.saham.hr_system.modules.auth.service.implementation.AuthenticationServiceImpl;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class AuthenticationUnitTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private JwtUtilities jwtUtilities;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @BeforeEach
    void setUp(){}

    @Test
    void testAuthenticationSuccess(){}

    @Test
    void testAuthenticationFailure(){}
}
