package com.saham.hr_system.initializer;

import com.saham.hr_system.model.Employee;
import com.saham.hr_system.model.EmployeeBalance;
import com.saham.hr_system.model.Role;
import com.saham.hr_system.model.RoleName;
import com.saham.hr_system.repository.EmployeeBalanceRepository;
import com.saham.hr_system.repository.EmployeeRepository;
import com.saham.hr_system.repository.RoleRepository;
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

        Role employeeRole = roleRepository.findByRoleName(RoleName.EMPLOYEE.name()).orElseThrow();
        Role managerRole = roleRepository.findByRoleName(RoleName.MANAGER.name()).orElseThrow();
        Role hrRole = roleRepository.findByRoleName(RoleName.HR.name()).orElseThrow();

        Employee savedManager = null;
        Employee savedEmployee = null;
        Employee savedHR = null;

        // Manager

        if (employeeRepository.findByEmail("Ciryane@saham.com").isEmpty()) {

            // Default manager
            Employee manager = new Employee();
            manager.setFirstName("Ciryane");
            manager.setLastName("El Khiati");
            manager.setEmail("Ciryane@saham.com");
            manager.setMatriculation("SAHAMEMP006");
            manager.setPassword(passwordEncoder.encode("cyriane2025"));
            manager.setRoles(List.of(employeeRole, managerRole, hrRole));
            manager.setEntity("Saham Horiwzon");
            manager.setJoinDate(LocalDate.of(2017, 7, 1));
            manager.setManager(null);
            savedManager = employeeRepository.save(manager);

            // Default Balance:
            EmployeeBalance managerBalance = new EmployeeBalance();
            managerBalance.setInitialBalance(30);
            managerBalance.setMonthlyBalance(2);
            managerBalance.setAccumulatedBalance(10);
            managerBalance.setYear(2025);
            managerBalance.setLastUpdated(LocalDateTime.now());
            managerBalance.setUsedBalance(0);
            managerBalance.setEmployee(savedManager);

            employeeBalanceRepository.save(managerBalance);
        }
        if (employeeRepository.findByEmail("samid@saham.com").isEmpty()) {

            // Default employee
            Employee manager = new Employee();
            manager.setFirstName("Salaheddine");
            manager.setLastName("Samid");
            manager.setEmail("samid@saham.com");
            manager.setMatriculation("SAHAMEMP001");
            manager.setPassword(passwordEncoder.encode("salaheddine2025"));
            manager.setRoles(List.of(employeeRole));
            manager.setEntity("Saham Group");
            manager.setJoinDate(LocalDate.of(2025, 11, 1));
            manager.setManager(savedManager);
            savedEmployee = employeeRepository.save(manager);

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
        }


        // HR:
        if (employeeRepository.findByEmail("myriam@saham.com").isEmpty()) {

            // Default HR
            Employee hr = new Employee();
            hr.setFirstName("Myriam");
            hr.setLastName("Wargane");
            hr.setEmail("myriam@saham.com");
            hr.setMatriculation("EMP001");
            hr.setPassword(passwordEncoder.encode("myriam2025"));
            hr.setRoles(List.of(employeeRole, hrRole));
            hr.setEntity("Saham Group");
            hr.setJoinDate(LocalDate.of(2018, 3, 12));
            hr.setManager(savedManager);
            savedHR =  employeeRepository.save(hr);

            // Default Balance:
            EmployeeBalance managerBalance = new EmployeeBalance();
            managerBalance.setInitialBalance(30);
            managerBalance.setMonthlyBalance(2);
            managerBalance.setAccumulatedBalance(10);
            managerBalance.setYear(2025);
            managerBalance.setLastUpdated(LocalDateTime.now());
            managerBalance.setUsedBalance(0);
            managerBalance.setEmployee(savedHR);

            employeeBalanceRepository.save(managerBalance);
        }
    }
}
