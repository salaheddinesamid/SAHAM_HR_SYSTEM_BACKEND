package com.saham.hr_system.service.implementation;

import com.saham.hr_system.dto.EmployeeDetailsDto;
import com.saham.hr_system.model.Employee;
import com.saham.hr_system.repository.EmployeeRepository;
import com.saham.hr_system.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public EmployeeDetailsDto getEmployeeDetails(String email) {
        // Fetch teh employee from the database:
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow();

        // Map the employee entity to EmployeeDetailsDto:
        return new EmployeeDetailsDto(
                employee
        );
    }
}
