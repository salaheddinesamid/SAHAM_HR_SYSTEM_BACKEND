package com.saham.hr_system.initializer;

import com.saham.hr_system.model.Employee;
import com.saham.hr_system.model.Role;
import com.saham.hr_system.model.RoleName;
import com.saham.hr_system.repository.EmployeeRepository;
import com.saham.hr_system.repository.RoleRepository;
import com.saham.hr_system.utils.PasswordGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Initializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordGenerator passwordGenerator;

    private static final List<RoleName> DEFAULT_ROLES = List.of(
            RoleName.EMPLOYEE,
            RoleName.MANAGER,
            RoleName.HR,
            RoleName.ADMIN
    );

    @Override
    public void run(String... args) {
        initializeRoles();
        initializeEmployees();
    }

    private void initializeRoles() {
        for (RoleName roleName : DEFAULT_ROLES) {
            roleRepository.findByRoleName(roleName.name())
                    .orElseGet(() -> {
                        Role newRole = new Role();
                        newRole.setRoleName(roleName.name());
                        return roleRepository.save(newRole);
                    });
        }
    }

    private void initializeEmployees() {
        if (employeeRepository.findByEmail("salaheddine.samid@saham.com").isEmpty()) {

            Role employeeRole = roleRepository.findByRoleName(RoleName.EMPLOYEE.name()).orElseThrow();
            Role managerRole = roleRepository.findByRoleName(RoleName.MANAGER.name()).orElseThrow();

            // Default manager
            Employee manager = new Employee();
            manager.setFirstName("Salaheddine");
            manager.setLastName("Samid");
            manager.setEmail("salaheddine.samid@saham.com");
            manager.setMatriculation("EMP001");
            manager.setPassword(passwordGenerator.generatePassword(manager.getFirstName(), manager.getEmail()));
            manager.setRoles(List.of(employeeRole, managerRole));
            manager.setEntity("Saham Group");
            manager.setJoinDate(LocalDate.of(2020, 1, 1));
            manager.setManager(null);
            employeeRepository.save(manager);

            // Default employee
            Employee employee = new Employee();
            employee.setFirstName("John");
            employee.setLastName("Doe");
            employee.setEmail("john.doe@saham.com");
            employee.setMatriculation("EMP002");
            employee.setPassword(passwordGenerator.generatePassword(employee.getFirstName(), employee.getEmail()));
            employee.setRoles(List.of(employeeRole));
            employee.setManager(manager);
            employee.setEntity("Saham Group");
            employee.setJoinDate(LocalDate.of(2024, 1, 15));
            employeeRepository.save(employee);
        }
    }
}
