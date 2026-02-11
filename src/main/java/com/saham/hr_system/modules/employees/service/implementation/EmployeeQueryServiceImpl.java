package com.saham.hr_system.modules.employees.service.implementation;

import com.saham.hr_system.modules.employees.dto.EmployeeDetailsDto;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.model.EmployeeBalance;
import com.saham.hr_system.modules.employees.model.Role;
import com.saham.hr_system.modules.employees.repository.EmployeeBalanceRepository;
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
    private final EmployeeBalanceRepository employeeBalanceRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public EmployeeQueryServiceImpl(EmployeeRepository employeeRepository, EmployeeBalanceRepository employeeBalanceRepository, RoleRepository roleRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeBalanceRepository = employeeBalanceRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Page<EmployeeDetailsDto> getAllEmployees(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        // fetch the employees from the database:
        Page<Employee> employees =
                employeeRepository.findAll(pageable);
        return employees.map(employee -> {
            // Fetch the balance:
            EmployeeBalance balance = employee.getEmployeeBalance() != null ? employee.getEmployeeBalance() : employeeBalanceRepository.findByEmployee(employee).orElse(null);
            return new EmployeeDetailsDto(employee, balance);
        });

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
    public Employee getManager(Long id) {
        Role managerRole = roleRepository.findByRoleName("MANAGER")
                .orElseThrow(()-> new RuntimeException("Manager role not found"));

        return
                employeeRepository
                        .findByRolesAndId(List.of(managerRole),id).orElseThrow(()-> new RuntimeException("Manager not found"));
    }

    @Override
    public List<Employee> getAllManagers() {
        Role managerRole = roleRepository.findByRoleName("MANAGER")
                .orElseThrow(()-> new RuntimeException("Manager role not found"));
        return
                employeeRepository.findAllByRoles(List.of(managerRole));
    }

    @Override
    public Employee getEmployeeProfileDetails(String email) {
        return null;
    }

    private String[] splitFullName(String fullName){
        String[] splitName = fullName.split(" ");
        String firstName = "";
        String lastName = "" ;

        if(splitName.length > 2){
            firstName = splitName[0];
            lastName = splitName[1] + " " + splitName[2];
        } else if (splitName.length == 2) {
            firstName = fullName.split(" ")[0];
            lastName = fullName.split(" ")[1];
        }
        return new String[]{firstName, lastName};
    }
}
