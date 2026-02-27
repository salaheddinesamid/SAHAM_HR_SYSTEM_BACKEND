package com.saham.hr_system.initializer;

import com.saham.hr_system.modules.employees.dto.*;
import com.saham.hr_system.modules.employees.model.*;
import com.saham.hr_system.modules.employees.repository.*;
import com.saham.hr_system.modules.employees.service.implementation.CeoAdderServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class Initializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeProfessionalDetailsRepository employeeProfessionalDetailsRepository;
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
            // Professional Details:
            EmployeeProfessionalDetails adminProfessionalDetails = new EmployeeProfessionalDetails();
            adminProfessionalDetails.setMatriculation("EMPADMIN");
            adminProfessionalDetails.setEntity(EmployeeEntity.SAHAM_HORIZON);
            admin.setFirstName("Admin");
            admin.setLastName("Admin");
            admin.setSex(EmployeeSex.MALE);
            admin.setCIN("");
            admin.setEmail("admin.hr@saham.com");
            admin.setPassword(passwordEncoder.encode("admin2025"));
            admin.setRoles(List.of(adminRole));
            admin.setStatus(EmployeeStatus.AVAILABLE);
            admin.setAccountLocked(false);
            EmployeeProfessionalDetails savedProfessionalDetails = employeeProfessionalDetailsRepository.save(adminProfessionalDetails);


            admin.setEmployeeProfessionalDetails(savedProfessionalDetails);
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
        ceoProfessionalDetails.setDepartment("CEO_OFFICE");
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
                "MALE",
                "AAAAA",
                "",
                "Morocco",
                LocalDate.of(1990, 1, 1),
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
}
