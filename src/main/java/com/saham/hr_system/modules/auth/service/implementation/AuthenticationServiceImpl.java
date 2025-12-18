package com.saham.hr_system.modules.auth.service.implementation;

import com.saham.hr_system.exception.BadCredentialsException;
import com.saham.hr_system.exception.UserNotFoundException;
import com.saham.hr_system.jwt.JwtUtilities;
import com.saham.hr_system.modules.auth.dto.BearerToken;
import com.saham.hr_system.modules.auth.dto.LoginRequestDto;
import com.saham.hr_system.modules.auth.dto.LoginResponseDto;
import com.saham.hr_system.modules.auth.dto.MicrosoftGraphAccessToken;
import com.saham.hr_system.modules.auth.service.AuthenticationService;
import com.saham.hr_system.modules.employees.dto.EmployeeDetailsDto;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.model.EmployeeBalance;
import com.saham.hr_system.modules.employees.model.Role;
import com.saham.hr_system.modules.employees.repository.EmployeeBalanceRepository;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeBalanceRepository employeeBalanceRepository;
    private final JwtUtilities jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;

    public AuthenticationServiceImpl(EmployeeRepository employeeRepository, EmployeeBalanceRepository employeeBalanceRepository, JwtUtilities jwtUtils, PasswordEncoder passwordEncoder, RestTemplate restTemplate) {
        this.employeeRepository = employeeRepository;
        this.employeeBalanceRepository = employeeBalanceRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.restTemplate = restTemplate;
    }

    @Override
    public LoginResponseDto authenticate(LoginRequestDto requestDto) {
        // Fetch employee from db:
        Employee employee = employeeRepository.findByEmail(requestDto.getEmail()).orElse(null);

        // Fetch balance:
        assert employee != null;
        EmployeeBalance balance = employee.getEmployeeBalance();
        assert balance == null;
        balance = employeeBalanceRepository.findByEmployee(employee).orElse(null);
        // In case the user does not exist:
        // Verify credentials:
        if(!passwordEncoder.matches(requestDto.getPassword(), employee.getPassword())) {
            throw new BadCredentialsException("Invalid credentials for user: " + requestDto.getEmail());
        }

        // Fetch the roles:
        List<Role> roles = employee.getRoles();

        List<String> roleNames = roles.stream()
                .map(Role::getRoleName)
                .toList();
        // Generate tokens:
        String accessToken = jwtUtils.generateToken(employee.getEmail(), roleNames);
        String refreshToken = "";

        // Generate bearer token object:
        BearerToken bearerToken = new BearerToken(accessToken,refreshToken);

        // Generate employee details:
        EmployeeDetailsDto employeeDetails = new EmployeeDetailsDto(
                employee,
                balance
        );
        return new LoginResponseDto(
                bearerToken,
                employeeDetails,
                ""
        );
    }

    private MicrosoftGraphAccessToken microsoftGraphAuthentication(){
        return null;
    }
}
