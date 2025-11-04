package com.saham.hr_system.initializer;

import com.saham.hr_system.model.Employee;
import com.saham.hr_system.model.EmployeeBalance;
import com.saham.hr_system.model.Role;
import com.saham.hr_system.model.RoleName;
import com.saham.hr_system.repository.EmployeeBalanceRepository;
import com.saham.hr_system.repository.EmployeeRepository;
import com.saham.hr_system.repository.RoleRepository;
import com.saham.hr_system.utils.PasswordGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Initializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordGenerator passwordGenerator;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeBalanceRepository employeeBalanceRepository;

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

            // Default Balance:
            EmployeeBalance managerBalance = new EmployeeBalance();
            managerBalance.setInitialBalance(30);
            managerBalance.setMonthlyBalance(2);
            managerBalance.setAccumulatedBalance(10);
            managerBalance.setYear(2025);
            managerBalance.setLastUpdated(LocalDateTime.now());
            managerBalance.setUsedBalance(0);
            managerBalance.setEmployee(manager);

            employeeBalanceRepository.save(managerBalance);


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

            // Default Balance:
            EmployeeBalance employeeBalance = new EmployeeBalance();
            employeeBalance.setInitialBalance(30);
            employeeBalance.setMonthlyBalance(2);
            employeeBalance.setAccumulatedBalance(10);
            employeeBalance.setYear(2025);
            employeeBalance.setLastUpdated(LocalDateTime.now());
            employeeBalance.setUsedBalance(0);
            employeeBalance.setEmployee(employee);
            employeeBalanceRepository.save(employeeBalance);
        }

        if (employeeRepository.findByEmail("admin@saham.com").isEmpty()) {

            Role employeeRole = roleRepository.findByRoleName(RoleName.ADMIN.name()).orElseThrow();
            Role managerRole = roleRepository.findByRoleName(RoleName.EMPLOYEE.name()).orElseThrow();

            // Default manager
            Employee admin = new Employee();
            admin.setFirstName("Admin");
            admin.setLastName("Samid");
            admin.setEmail("admin@saham.com");
            admin.setMatriculation("EMP001");
            admin.setPassword(passwordGenerator.generatePassword("Admin", "Samid"));
            admin.setRoles(List.of(employeeRole, managerRole));
            admin.setEntity("Saham Group");
            admin.setJoinDate(LocalDate.of(2020, 1, 1));
            admin.setManager(null);
            employeeRepository.save(admin);

            // Default Balance:
            EmployeeBalance managerBalance = new EmployeeBalance();
            managerBalance.setInitialBalance(30);
            managerBalance.setMonthlyBalance(2);
            managerBalance.setAccumulatedBalance(10);
            managerBalance.setYear(2025);
            managerBalance.setLastUpdated(LocalDateTime.now());
            managerBalance.setUsedBalance(0);
            managerBalance.setEmployee(admin);

            employeeBalanceRepository.save(managerBalance);
        }

        if (employeeRepository.findByEmail("test@saham.com").isEmpty()) {

            Role employeeRole = roleRepository.findByRoleName(RoleName.ADMIN.name()).orElseThrow();
            Role managerRole = roleRepository.findByRoleName(RoleName.EMPLOYEE.name()).orElseThrow();

            // Default manager
            Employee admin = new Employee();
            admin.setFirstName("Test");
            admin.setLastName("Test");
            admin.setEmail("test@saham.com");
            admin.setMatriculation("EMP001");
            admin.setPassword(passwordEncoder.encode("test2025"));
            admin.setRoles(List.of(employeeRole, managerRole));
            admin.setEntity("Saham Group");
            admin.setJoinDate(LocalDate.of(2020, 1, 1));
            admin.setManager(null);
            employeeRepository.save(admin);

            // Default Balance:
            EmployeeBalance managerBalance = new EmployeeBalance();
            managerBalance.setInitialBalance(30);
            managerBalance.setMonthlyBalance(2);
            managerBalance.setAccumulatedBalance(10);
            managerBalance.setYear(2025);
            managerBalance.setLastUpdated(LocalDateTime.now());
            managerBalance.setUsedBalance(0);
            managerBalance.setEmployee(admin);

            employeeBalanceRepository.save(managerBalance);
        }
    }
}
