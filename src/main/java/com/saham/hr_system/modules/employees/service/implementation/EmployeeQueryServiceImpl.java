package com.saham.hr_system.modules.employees.service.implementation;

import com.saham.hr_system.modules.employees.dto.EmployeeDetailsDto;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.model.Role;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.modules.employees.repository.RoleRepository;
import com.saham.hr_system.modules.employees.service.EmployeeQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeQueryServiceImpl implements EmployeeQueryService {
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public EmployeeQueryServiceImpl(EmployeeRepository employeeRepository, RoleRepository roleRepository) {
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Page<EmployeeDetailsDto> getAllEmployees(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        // fetch the employees from the database:
        Page<Employee> employees =
                employeeRepository.findAll(pageable);
        return employees.map(employee -> new EmployeeDetailsDto(employee, employee.getEmployeeBalance()));

    }

    @Override
    public boolean verifyManager(String fullName) {
        Role managerRole = roleRepository.findByRoleName("MANAGER")
                .orElseThrow(()-> new RuntimeException("Manager role not found"));

        String firstName = fullName.split(" ")[0];
        String lastName = fullName.split(" ")[1];

        return employeeRepository.existsByFirstNameAndLastName(
                firstName, lastName
        );
    }

    @Override
    public Employee getManager(String fullName) {
        String firstName = fullName.split(" ")[0];
        String lastName = fullName.split(" ")[1];

        Role managerRole = roleRepository.findByRoleName("MANAGER")
                .orElseThrow(()-> new RuntimeException("Manager role not found"));

        return
                employeeRepository
                        .findByRolesAndFirstNameAndLastName(
                                List.of(managerRole),
                                firstName,
                                lastName
                        ).orElseThrow(()-> new RuntimeException("Manager not found"));
    }
}
