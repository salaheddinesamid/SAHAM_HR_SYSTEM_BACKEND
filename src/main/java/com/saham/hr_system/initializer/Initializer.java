package com.saham.hr_system.initializer;

import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.model.EmployeeBalance;
import com.saham.hr_system.modules.employees.model.Role;
import com.saham.hr_system.modules.employees.model.RoleName;
import com.saham.hr_system.modules.employees.repository.EmployeeBalanceRepository;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.modules.employees.repository.RoleRepository;
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

        Employee ciryane = null;
        Employee wijdane = null;
        Employee myriam = null;

        // Ciryane EL KHIATI - Manager
        if (employeeRepository.findByEmail("cyriane.elkhiati@Saham.com").isEmpty()) {

            // Default manager
            Employee manager = new Employee();
            manager.setFirstName("Ciryane"); // first name
            manager.setLastName("EL KHIATI"); // last name
            manager.setEmail("cyriane.elkhiati@Saham.com"); // email
            manager.setMatriculation("SAHAMEMP006"); // Matriculation:
            manager.setPassword(passwordEncoder.encode("cyriane2025"));
            manager.setRoles(List.of(employeeRole, managerRole)); // employee and manager role
            manager.setEntity("Saham Horizon"); // Entity
            manager.setOccupation("Directrice du Capital Humain"); // Occupation
            manager.setJoinDate(LocalDate.of(2025, 4, 7)); // join date
            manager.setManager(null);
            ciryane = employeeRepository.save(manager);

            // Default Balance:
            EmployeeBalance managerBalance = new EmployeeBalance();
            managerBalance.setInitialBalance(22); // Droit Annuel
            managerBalance.setMonthlyBalance(2); // Droit Mensuel*
            managerBalance.setDaysLeft(0); // Solde 2024
            managerBalance.setAccumulatedBalance(10); // Jours Accumulés
            managerBalance.setYear(2024); // Année
            managerBalance.setLastUpdated(LocalDateTime.now());
            managerBalance.setUsedBalance(21); // Pris
            managerBalance.setEmployee(ciryane); // set the employee

            employeeBalanceRepository.save(managerBalance); // save the employee balance:
        }

        // Miryam WARGANE - HR:
        if (employeeRepository.findByEmail("myriam.wargane@Saham.com").isEmpty()) {

            // Default HR
            Employee hr = new Employee();
            hr.setFirstName("Myriam");
            hr.setLastName("Wargane");
            hr.setEmail("myriam.wargane@Saham.com");
            hr.setMatriculation("EMP001");
            hr.setPassword(passwordEncoder.encode("myriam2025"));
            hr.setRoles(List.of(employeeRole, hrRole, managerRole));
            hr.setEntity("SAHAM Horizon"); // set the entity
            hr.setOccupation("Responsable des Ressources Humaines ");
            hr.setJoinDate(LocalDate.of(2014, 3, 3)); // join date
            hr.setManager(ciryane);
            myriam =  employeeRepository.save(hr);

            // Default Balance:
            EmployeeBalance balance = new EmployeeBalance();
            balance.setInitialBalance(29); // Droit Annuel
            balance.setMonthlyBalance(2); // Droit Mensuel
            balance.setAccumulatedBalance(14); // Jours Accumulés
            balance.setYear(2024); // Année
            balance.setDaysLeft(48); // Solde 2024
            balance.setLastUpdated(LocalDateTime.now());
            balance.setUsedBalance(21); // pris
            balance.setEmployee(myriam);

            employeeBalanceRepository.save(balance);
        }

        // Wijdane Sabir - HR:
        if (employeeRepository.findByEmail("wijdane.sabir@Saham.com").isEmpty()) {

            // Default HR
            Employee hr = new Employee();
            hr.setFirstName("Wijdane");
            hr.setLastName("Sabir");
            hr.setEmail("wijdane.sabir@Saham.com");
            hr.setMatriculation("EMP001");
            hr.setPassword(passwordEncoder.encode("wijdane2025"));
            hr.setRoles(List.of(employeeRole, hrRole));
            hr.setEntity("SAHAM Horizon"); // set the entity
            hr.setOccupation("HR Generalist"); // set the occupation
            hr.setJoinDate(LocalDate.of(2022, 4, 1)); // join date
            hr.setManager(myriam);
            wijdane =  employeeRepository.save(hr);

            // Default Balance:
            EmployeeBalance balance = new EmployeeBalance();
            balance.setInitialBalance(26); // Droit Annuel
            balance.setMonthlyBalance(3); // Droit Mensuel
            balance.setAccumulatedBalance(23); // Jours Accumulés
            balance.setYear(2024); // Année
            balance.setDaysLeft(2); // Solde 2024
            balance.setLastUpdated(LocalDateTime.now());
            balance.setUsedBalance(25); // pris
            balance.setEmployee(wijdane);

            employeeBalanceRepository.save(balance);
        }
    }
}
