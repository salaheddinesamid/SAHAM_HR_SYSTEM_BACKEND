package com.saham.hr_system.modules.administration.mapper;

import com.saham.hr_system.modules.administration.dto.EmployeeBalanceDto;
import com.saham.hr_system.modules.administration.dto.NewEmployeeRequestDto;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.model.EmployeeBalance;
import com.saham.hr_system.modules.employees.model.EmployeeStatus;
import com.saham.hr_system.modules.employees.model.Role;
import com.saham.hr_system.modules.employees.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EmployeeMapper {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public EmployeeMapper(RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Employee mapToEmployee(NewEmployeeRequestDto requestDto) {
        Employee employee = new Employee();
        employee.setFirstName(requestDto.getFirstName());
        employee.setLastName(requestDto.getLastName());
        employee.setMatriculation(requestDto.getMatriculation());
        employee.setEmail(requestDto.getEmail());
        // set the encoded password:
        employee.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        // set the roles:
        requestDto.getRoles().forEach(r -> {
            Role role = roleRepository.findByRoleName(r).orElseThrow();
            employee.getRoles().add(role);
        });
        employee.setStatus(EmployeeStatus.AVAILABLE);
        employee.setEntity(requestDto.getEntity());
        employee.setOccupation(requestDto.getOccupation());

        return employee;
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
