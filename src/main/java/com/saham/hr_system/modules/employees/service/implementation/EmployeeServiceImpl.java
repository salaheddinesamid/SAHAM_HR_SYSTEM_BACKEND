package com.saham.hr_system.modules.employees.service.implementation;

import com.saham.hr_system.modules.employees.dto.EmployeeDetailsDto;
import com.saham.hr_system.modules.employees.dto.SubordinateDetailsResponseDto;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.model.EmployeeBalance;
import com.saham.hr_system.modules.employees.repository.EmployeeBalanceRepository;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.modules.employees.service.EmployeeService;
import org.hibernate.service.UnknownServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeBalanceRepository employeeBalanceRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeBalanceRepository employeeBalanceRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeBalanceRepository = employeeBalanceRepository;
    }

    @Override
    public EmployeeDetailsDto getEmployeeDetails(String email) {
        // Fetch teh employee from the database:
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow();

        // Fetch the balance:
        EmployeeBalance balance = employee.getEmployeeBalance() != null ? employee.getEmployeeBalance() : employeeBalanceRepository.findByEmployee(employee).orElse(null);

        // Map the employee entity to EmployeeDetailsDto:
        return new EmployeeDetailsDto(
                employee,
                balance
        );
    }

    @Override
    public List<SubordinateDetailsResponseDto> getSubordinates(String email) {
        // Fetch the manager:
        Employee manager =
                employeeRepository.findByEmail(email)
                        .orElseThrow(()-> new UsernameNotFoundException(email));

        List<Employee> subordinates = employeeRepository.findAllByManagerId(manager.getId());

        return
                subordinates.stream().map(SubordinateDetailsResponseDto::new).collect(Collectors.toList());
    }
}
