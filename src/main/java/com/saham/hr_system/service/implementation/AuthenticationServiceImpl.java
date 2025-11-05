package com.saham.hr_system.service.implementation;

import com.saham.hr_system.dto.*;
import com.saham.hr_system.exception.BadCredentialsException;
import com.saham.hr_system.exception.UserNotFoundException;
import com.saham.hr_system.jwt.JwtUtils;
import com.saham.hr_system.model.Employee;
import com.saham.hr_system.model.EmployeeBalance;
import com.saham.hr_system.model.Role;
import com.saham.hr_system.repository.EmployeeBalanceRepository;
import com.saham.hr_system.repository.EmployeeRepository;
import com.saham.hr_system.service.AuthenticationService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeBalanceRepository employeeBalanceRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;

    public AuthenticationServiceImpl(EmployeeRepository employeeRepository, EmployeeBalanceRepository employeeBalanceRepository, JwtUtils jwtUtils, PasswordEncoder passwordEncoder, RestTemplate restTemplate) {
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
        EmployeeBalance balance = employeeBalanceRepository.findByEmployee(employee).orElse(null);
        // In case the user does not exist:
        if(employee == null){
            throw new UserNotFoundException(requestDto.getEmail());
        }
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
