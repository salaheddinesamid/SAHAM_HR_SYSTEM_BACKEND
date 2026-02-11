package com.saham.hr_system.auth.unit;

import com.saham.hr_system.modules.auth.service.implementation.AuthenticationServiceImpl;
import com.saham.hr_system.modules.auth.service.implementation.UserDetailsServiceImpl;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class AuthenticationUnitTest {

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Test
    void testAuthenticationSuccess(){}

    @Test
    void testAuthenticationThrowsUserNotFoundException(){}

    @Test
    void testAuthenticationThrowsInvalidPasswordException(){}
}
