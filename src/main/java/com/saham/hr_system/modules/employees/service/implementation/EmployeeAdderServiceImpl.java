package com.saham.hr_system.modules.employees.service.implementation;

import com.saham.hr_system.modules.auth.service.implementation.EmployeePasswordSetupService;
import com.saham.hr_system.modules.employees.dto.*;
import com.saham.hr_system.modules.employees.exception.EmployeeAlreadyExistsException;
import com.saham.hr_system.modules.employees.mapper.EmployeeContactDetailsMapper;
import com.saham.hr_system.modules.employees.mapper.EmployeeMapper;
import com.saham.hr_system.modules.employees.mapper.EmployeeProfessionalDetailsMapper;
import com.saham.hr_system.modules.employees.mapper.EmployeeSocialDetailMapper;
import com.saham.hr_system.modules.employees.model.*;
import com.saham.hr_system.modules.employees.repository.*;
import com.saham.hr_system.modules.employees.service.EmployeeAdderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.CompletableFuture;


@Service
@Slf4j
public class EmployeeAdderServiceImpl implements EmployeeAdderService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeBalanceRepository employeeBalanceRepository;
    private final EmployeeProfessionalDetailsRepository employeeProfessionalDetailsRepository;
    private final EmployeeSocialDetailsRepository employeeSocialDetailsRepository;
    private final EmployeeContactDetailsRepository employeeContactDetailsRepository;
    private final EmployeeMapper employeeMapper;
    private final EmployeeProfessionalDetailsMapper employeeProfessionalDetailsMapper;
    private final EmployeeSocialDetailMapper employeeSocialDetailMapper;
    private final EmployeeContactDetailsMapper employeeContactDetailsMapper;
    private final EmployeeQueryServiceImpl employeeQueryService;
    private final EmployeePasswordSetupService employeePasswordSetupService;
    private final NewEmployeeEmailSenderImpl newEmployeeEmailSender;

    @Autowired
    public EmployeeAdderServiceImpl(EmployeeRepository employeeRepository, EmployeeBalanceRepository employeeBalanceRepository, EmployeeProfessionalDetailsRepository employeeProfessionalDetailsRepository, EmployeeSocialDetailsRepository employeeSocialDetailsRepository, EmployeeContactDetailsRepository employeeContactDetailsRepository, RoleRepository roleRepository, EmployeeMapper employeeMapper, EmployeeProfessionalDetailsMapper employeeProfessionalDetailsMapper, EmployeeSocialDetailMapper employeeSocialDetailMapper, EmployeeContactDetailsMapper employeeContactDetailsMapper, EmployeeQueryServiceImpl employeeQueryService, EmployeePasswordSetupService employeePasswordSetupService, NewEmployeeEmailSenderImpl newEmployeeEmailSender) {
        this.employeeRepository = employeeRepository;
        this.employeeBalanceRepository = employeeBalanceRepository;
        this.employeeProfessionalDetailsRepository = employeeProfessionalDetailsRepository;
        this.employeeSocialDetailsRepository = employeeSocialDetailsRepository;
        this.employeeContactDetailsRepository = employeeContactDetailsRepository;
        this.employeeMapper = employeeMapper;
        this.employeeProfessionalDetailsMapper = employeeProfessionalDetailsMapper;
        this.employeeSocialDetailMapper = employeeSocialDetailMapper;
        this.employeeContactDetailsMapper = employeeContactDetailsMapper;
        this.employeeQueryService = employeeQueryService;
        this.employeePasswordSetupService = employeePasswordSetupService;
        this.newEmployeeEmailSender = newEmployeeEmailSender;
    }

    @Override
    public boolean verify() {
        return false;
    }

    /**
     * Adds a new employee to the system. This method performs the following steps:
     * <ul>
     *     <li>Checks if an employee with the same matriculation number already exists. If it does, an exception is thrown.</li>
     *     <li>Maps the incoming NewEmployeeDto to an Employee entity.</li>
     *     <li>Maps and saves the employee's professional details, social details, and contact details to their respective repositories.</li>
     *     <li>Creates and saves the employee's balance to the EmployeeBalanceRepository.</li>
     *     <li>Attaches the saved professional details, social details, contact details, and balance to the employee entity.</li>
     *     <li>Saves the employee entity to the EmployeeRepository.</li>
     *     <li>Returns an EmployeeDetailsDto containing the details of the newly added employee.</li>
     * </ul>
     * @param newEmployeeRequestDto
     * @return
     */
    @Override
    @Transactional
    public EmployeeDetailsDto add(NewEmployeeDto newEmployeeRequestDto) {
        // Check if the employee already exists by matriculation
        if(employeeRepository.existsByEmployeeProfessionalDetails_Matriculation(newEmployeeRequestDto.getProfessionalDetailsDto().getMatriculation())
                || employeeRepository.existsByEmail(newEmployeeRequestDto.getProfessionalDetailsDto().getProfessionalEmail())
        ) {
            throw new EmployeeAlreadyExistsException(
                    "La création de l'employé a échoué : une valeur unique (email, matricule ou CIN) est déjà utilisée."
            );
        }
        // Create new employee
        Map<String, Object> mappedEmployee = employeeMapper.mapToEmployee(newEmployeeRequestDto);
        Employee employee = mappedEmployee.get("mappedEmployee") != null ? (Employee) mappedEmployee.get("mappedEmployee") : null;
        log.info("Mapped employee: " + employee + "with raw password: " + mappedEmployee.get("rawPassword"));

        assert employee != null;
        // Create and save employee professional details
        EmployeeProfessionalDetails employeeProfessionalDetails =
                employeeProfessionalDetailsMapper.mapToEmployeeProfessionalDetails(newEmployeeRequestDto.getProfessionalDetailsDto(), false);
        employee.setEmail(employeeProfessionalDetails.getProfessionalEmail());
        EmployeeProfessionalDetails savedProfessionalDetails = employeeProfessionalDetailsRepository.save(employeeProfessionalDetails);
        // Create and save employee social details
        EmployeeSocialDetails employeeSocialDetails = employeeSocialDetailMapper.mapToEmployeeSocialDetails(
                newEmployeeRequestDto.getEmployeeSocialDetailsDto()
        );
        EmployeeSocialDetails savedSocialDetails = employeeSocialDetailsRepository.save(employeeSocialDetails);

        // Create and save employee contact details:
        EmployeeContactDetails employeeContactDetails = employeeContactDetailsMapper.mapToEmployeeContactDetails(
                newEmployeeRequestDto.getEmployeeContactDetailsDto()
        );
        EmployeeContactDetails savedContactDetails = employeeContactDetailsRepository.save(employeeContactDetails);
        // create  and save new balance:
        EmployeeBalance employeeBalance =
                employeeMapper.mapToEmployeeBalanceDto(newEmployeeRequestDto.getEmployeeBalance());
        EmployeeBalance savedBalance =
                employeeBalanceRepository.save(employeeBalance);

        // attach the balance to the employee:
        employee.setEmployeeBalance(savedBalance);
        // attach the professional details to the employee:
        employee.setEmployeeProfessionalDetails(savedProfessionalDetails);
        // attach the social details to the employee:
        employee.setEmployeeSocialDetails(savedSocialDetails);
        // attach the contact details to the employee:
        employee.setEmployeeContactDetails(savedContactDetails);

        Employee savedEmployee = employeeRepository.save(employee);

        // initiate setup of the employee password:
        String link = employeePasswordSetupService.initiatePasswordSetup(savedEmployee.getEmail());
        // notify the employee:
        CompletableFuture.runAsync(()->{
                try{
                    newEmployeeEmailSender.sendWelcomeEmail(employee.getEmail(), employee, link);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
        });
        // notify the employee and Manager:
        return new EmployeeDetailsDto(savedEmployee);
    }
}
