package com.saham.hr_system.modules.employees.mapper;

import com.saham.hr_system.modules.employees.dto.EmployeeBalanceDto;
import com.saham.hr_system.modules.employees.dto.NewEmployeeDto;
import com.saham.hr_system.modules.employees.dto.NewEmployeeProfessionalDetailsDto;
import com.saham.hr_system.modules.employees.model.*;
import com.saham.hr_system.modules.employees.repository.RoleRepository;
import com.saham.hr_system.modules.employees.utils.EmployeePasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class EmployeeMapper {
    private final RoleRepository roleRepository;
    private final EmployeePasswordGenerator employeePasswordGenerator;

    @Autowired
    public EmployeeMapper(RoleRepository roleRepository, EmployeePasswordGenerator employeePasswordGenerator) {
        this.roleRepository = roleRepository;
        this.employeePasswordGenerator = employeePasswordGenerator;
    }

    public Map<String, Object> mapToEmployee(NewEmployeeDto requestDto) {
        Employee employee = new Employee();
        employee.setFirstName(requestDto.getFirstName());
        employee.setLastName(requestDto.getLastName());
        employee.setSex(EmployeeSex.valueOf(requestDto.getSex()));
        employee.setCIN(requestDto.getCIN());
        employee.setEmail(requestDto.getEmail());
        Map<String, String> generatedPassword = employeePasswordGenerator.generatePassword(employee.getFullName());
         // set the encoded password:
        employee.setPassword(generatedPassword.get("encodedPassword"));
        // set the roles:
        requestDto.getRoles().forEach(r -> {
            Role role = roleRepository.findByRoleName(r).orElseThrow();
            employee.getRoles().add(role);
        });
        employee.setStatus(EmployeeStatus.AVAILABLE);
        return Map.of(
                "mappedEmployee", employee,
                "rawPassword" , generatedPassword.get("rawPassword")
        );
    }

    public EmployeeBalance mapToEmployeeBalanceDto(EmployeeBalanceDto balanceDto) {
        // create new balance:
        EmployeeBalance employeeBalance = new EmployeeBalance();
        employeeBalance.setYear(balanceDto.getYear());
        employeeBalance.setCurrentBalance(balanceDto.getCurrentBalance());
        employeeBalance.setAnnualBalance(balanceDto.getAnnualBalance());
        employeeBalance.setUsedBalance(balanceDto.getUsedBalance());
        employeeBalance.setAccumulatedBalance(balanceDto.getAccumulatedBalance());
        employeeBalance.setLastUpdated(LocalDateTime.now());

        return employeeBalance;
    }
}
