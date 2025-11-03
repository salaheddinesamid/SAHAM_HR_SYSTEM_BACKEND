package com.saham.hr_system.service.implementation;

import com.saham.hr_system.dto.EmployeeDetailsDto;
import com.saham.hr_system.model.Employee;
import com.saham.hr_system.model.EmployeeBalance;
import com.saham.hr_system.repository.EmployeeBalanceRepository;
import com.saham.hr_system.repository.EmployeeRepository;
import com.saham.hr_system.service.EmployeeService;
import org.springframework.stereotype.Service;

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
        EmployeeBalance balance = employeeBalanceRepository.findByEmployee(employee).orElseThrow();

        // Map the employee entity to EmployeeDetailsDto:
        return new EmployeeDetailsDto(
                employee,
                balance
        );
    }
}
