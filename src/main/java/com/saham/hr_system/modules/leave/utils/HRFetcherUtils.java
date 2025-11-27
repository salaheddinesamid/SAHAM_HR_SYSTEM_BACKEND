package com.saham.hr_system.modules.leave.utils;

import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.model.Role;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.modules.employees.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HRFetcherUtils {

    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public HRFetcherUtils(EmployeeRepository employeeRepository, RoleRepository roleRepository) {
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
    }

    public List<String> fetchHREmail(){
        // fetch HR role:
        Role hrRole =
                roleRepository.findByRoleName("HR").orElseThrow();
        // fetch all HR:
        List<Employee> hrs =
                employeeRepository.findAllByRoles(List.of(hrRole));
        // return emails:
        return
                hrs.stream()
                        .map(Employee::getEmail)
                        .toList();
    }
}
