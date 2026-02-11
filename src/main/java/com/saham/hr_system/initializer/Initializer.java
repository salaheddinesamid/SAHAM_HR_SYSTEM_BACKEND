package com.saham.hr_system.initializer;

import com.saham.hr_system.modules.employees.dto.*;
import com.saham.hr_system.modules.employees.model.*;
import com.saham.hr_system.modules.employees.repository.*;
import com.saham.hr_system.modules.employees.service.implementation.CeoAdderServiceImpl;
import com.saham.hr_system.modules.employees.service.implementation.EmployeeAdderServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class Initializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final CeoAdderServiceImpl ceoAdderService;

    private static final List<RoleName> DEFAULT_ROLES = List.of(
            RoleName.CEO,
            RoleName.EMPLOYEE,
            RoleName.MANAGER,
            RoleName.HR,
            RoleName.ADMIN
    );

    @Override
    @Transactional
    public void run(String... args) {
        initializeRoles();
        initializeAdmin();
        initializeCEO();
        // initializeEmployees();
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

    private void initializeAdmin(){
        Employee admin = new Employee();
        Role adminRole  = roleRepository.findByRoleName("ADMIN")
                        .orElseThrow();
        if(!employeeRepository.existsByEmail("admin.hr@saham.com")){
            admin.setFirstName("Admin");
            admin.setLastName("Admin");
            admin.setCIN("");
            admin.setEmail("admin.hr@saham.com");
            admin.setPassword(passwordEncoder.encode("admin2025"));
            admin.setRoles(List.of(adminRole));
            admin.setStatus(EmployeeStatus.AVAILABLE);

            employeeRepository.save(admin);
        }

    }

    @Transactional
    protected void initializeCEO() {

        Role ceoRole = roleRepository.findByRoleName("CEO")
                .orElseThrow(() -> new IllegalStateException("ROLE_CEO not found"));
        Role managerRole = roleRepository.findByRoleName("MANAGER")
                .orElseThrow(() -> new IllegalStateException("ROLE_CEO not found"));

        // 1️⃣ Check if CEO already exists
        boolean existingCEO =
                employeeRepository.existsByFirstNameAndLastName(
                        "Moulay Mhamed", "Elalamy"
                );

        if (existingCEO) {
            return; // CEO already initialized
        }

        // CEO professional details
        NewEmployeeProfessionalDetailsDto ceoProfessionalDetails = new NewEmployeeProfessionalDetailsDto();
        ceoProfessionalDetails.setProfessionalEmail("ceo@saham.com");
        ceoProfessionalDetails.setMatriculation("CEO001");
        ceoProfessionalDetails.setEntity("SAHAM_HORIZON");
        ceoProfessionalDetails.setManagerId(null);

        // CEO social details
        NewEmployeeSocialDetails ceoSocialDetails = new NewEmployeeSocialDetails();
        // CEO contact details
        NewEmployeeContactDetails ceoContactDetails = new NewEmployeeContactDetails();

        // CEO balance details
        EmployeeBalanceDto balanceDto = new EmployeeBalanceDto();
        balanceDto.setYear(Year.now().getValue());
        balanceDto.setAnnualBalance(30);
        balanceDto.setAccumulatedBalance(30);
        balanceDto.setUsedBalance(0);

        NewEmployeeDto newEmployeeDto = new NewEmployeeDto(
                "Moulay Mhamed",
                "Elalamy",
                "AAAAA",
                "SINGLE",
                0,
                "ceo@saham.com",
                ceoProfessionalDetails,
                ceoSocialDetails,
                ceoContactDetails,
                List.of("CEO","MANAGER", "EMPLOYEE"),
                balanceDto

        );

        ceoAdderService.add(newEmployeeDto);
    }

    /*
    @Transactional
    protected void initializeEmployees() {

        Role employeeRole = roleRepository.findByRoleName(RoleName.EMPLOYEE.name()).orElseThrow();
        Role managerRole = roleRepository.findByRoleName(RoleName.MANAGER.name()).orElseThrow();
        Role hrRole = roleRepository.findByRoleName(RoleName.HR.name()).orElseThrow();

        Employee ciryane = null;
        Employee wijdane = null;
        Employee myriam = null;

        // Ciryane EL KHIATI - Manager
        if (employeeRepository.findByEmail("cyriane.elkhiati@Saham.com").isEmpty()) {

            // Professional Details:
            NewEmployeeProfessionalDetailsDto professionalDetailsDto = new NewEmployeeProfessionalDetailsDto(
                    "SAHAMEMP006",
                    "Directrice du Capital Humain",
                    "Ressources Humaines",
                    "SAHAM_HORIZON",
                    "Moulay Mhammed Elalamy",
                    LocalDate.of(2025,07,10),
                    "Casablanca",
                    "00",
                    "cyriane.elkhiati@Saham.com",
                    "00",
                    ""
            );

            // Social Details:
            NewEmployeeSocialDetails socialDetails = new NewEmployeeSocialDetails(
                    "CNSS654321",
                    "CIMR654321",
                    "INS654321",
                    ""
            );

            // Contact Details:
            NewEmployeeContactDetails contactDetails = new NewEmployeeContactDetails(
                    "Dad",
                    "+212612345678"
            );

            // Balance Details:
            EmployeeBalanceDto balanceDetails = new EmployeeBalanceDto(
                    2025,
                    30,
                    30,
                    0,
                    0
            );

            // Default Details:
            NewEmployeeDto employeeDto = new NewEmployeeDto(
                    "Ciryane",
                    "EL KHIATI",
                    "AAAAAA",
                    "MARRIED",
                    0,
                    "cyriane.elkhiati@Saham.com",
                    professionalDetailsDto,
                    socialDetails,
                    contactDetails,
                    List.of("EMPLOYEE", "MANAGER"),
                    balanceDetails
            );
            // Create new employee:
            EmployeeDetailsDto employeeDetailsDto = employeeAdderService.add(employeeDto);

            // Default Balance:
            EmployeeBalance managerBalance = new EmployeeBalance();
            managerBalance.setAnnualBalance(22); // Droit Annuel
            managerBalance.setMonthlyBalance(2); // Droit Mensuel*
            managerBalance.setCurrentBalance(0); // Solde 2024
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
            hr.setMatriculation("SAHAMEMP005");
            hr.setPassword(passwordEncoder.encode("myriam2025"));
            hr.setRoles(List.of(employeeRole, hrRole, managerRole));
            hr.setEntity("SAHAM Horizon"); // set the entity
            hr.setOccupation("Responsable des Ressources Humaines ");
            hr.setJoinDate(LocalDate.of(2014, 3, 3)); // join date
            hr.setManager(ciryane);
            hr.setStatus(EmployeeStatus.AVAILABLE);
            myriam =  employeeRepository.save(hr);

            // Default Balance:
            EmployeeBalance balance = new EmployeeBalance();
            balance.setAnnualBalance(29); // Droit Annuel
            balance.setMonthlyBalance(2); // Droit Mensuel
            balance.setAccumulatedBalance(14); // Jours Accumulés
            balance.setYear(2024); // Année
            balance.setCurrentBalance(48); // Solde 2024
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
            hr.setMatriculation("SAHAMEMP023");
            hr.setPassword(passwordEncoder.encode("wijdane2025"));
            hr.setRoles(List.of(employeeRole, hrRole));
            hr.setEntity("SAHAM Horizon"); // set the entity
            hr.setOccupation("HR Generalist"); // set the occupation
            hr.setJoinDate(LocalDate.of(2022, 4, 1)); // join date
            hr.setManager(myriam);
            hr.setStatus(EmployeeStatus.AVAILABLE);
            wijdane =  employeeRepository.save(hr);

            // Default Balance:
            EmployeeBalance balance = new EmployeeBalance();
            balance.setAnnualBalance(26); // Droit Annuel
            balance.setMonthlyBalance(3); // Droit Mensuel
            balance.setAccumulatedBalance(23); // Jours Accumulés
            balance.setYear(2024); // Année
            balance.setCurrentBalance(2); // Solde 2024
            balance.setLastUpdated(LocalDateTime.now());
            balance.setUsedBalance(25); // pris
            balance.setEmployee(wijdane);

            employeeBalanceRepository.save(balance);
        }
    }

     */
}
