package com.saham.hr_system.modules.employees.service.implementation;

import com.saham.hr_system.exception.UserNotFoundException;
import com.saham.hr_system.modules.employees.dto.*;
import com.saham.hr_system.modules.employees.model.*;
import com.saham.hr_system.modules.employees.repository.EmployeeBalanceRepository;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.modules.employees.repository.RoleRepository;
import com.saham.hr_system.modules.employees.service.EmployeeUpdateService;
import com.saham.hr_system.modules.employees.utils.EmployeeProfilePictureUploader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class EmployeeUpdateServiceImpl implements EmployeeUpdateService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeBalanceRepository employeeBalanceRepository;
    private final EmployeeQueryServiceImpl employeeQueryService;
    private final RoleRepository roleRepository;
    private final EmployeeProfilePictureUploader employeeProfilePictureUploader;

    public EmployeeUpdateServiceImpl(EmployeeRepository employeeRepository, EmployeeBalanceRepository employeeBalanceRepository, EmployeeQueryServiceImpl employeeQueryService, RoleRepository roleRepository, EmployeeProfilePictureUploader employeeProfilePictureUploader) {
        this.employeeRepository = employeeRepository;
        this.employeeBalanceRepository = employeeBalanceRepository;
        this.employeeQueryService = employeeQueryService;
        this.roleRepository = roleRepository;
        this.employeeProfilePictureUploader = employeeProfilePictureUploader;
    }

    @Override
    @Transactional
    public EmployeeDetailsDto updateEmployee(Long employeeId, UpdateEmployeeDto updateEmployeeDto) {

        // fetch the employee from db:
        Employee employee = employeeRepository
                .findById(employeeId).orElseThrow();
        if(updateEmployeeDto.getFirstName() != null){
            employee.setFirstName(updateEmployeeDto.getFirstName());
        }
        if(updateEmployeeDto.getLastName() != null){
            employee.setLastName(updateEmployeeDto.getLastName());
        }
        // update the new email:
        if(updateEmployeeDto.getEmail() != null){
            employee.setEmail(updateEmployeeDto.getEmail());
        }
        // Update the CIN
        if(updateEmployeeDto.getCin() != null){
            employee.setCIN(updateEmployeeDto.getCin());
        }
        // Update the address
        if(updateEmployeeDto.getAddress() != null){
            employee.setAddress(updateEmployeeDto.getAddress());
        }

        // Update family status:
        if(updateEmployeeDto.getFamilyStatus() != null){
            employee.setFamilyStatus(EmployeeFamilyStatus.valueOf(updateEmployeeDto.getFamilyStatus()));
        }
        // Update nationality:
        if(updateEmployeeDto.getNationality() != null){
            employee.setNationality(updateEmployeeDto.getNationality());
        }
        // update the birthdate:
        if(updateEmployeeDto.getBirthDate() != null){
            employee.setBirthDate(updateEmployeeDto.getBirthDate());
        }
        // update the new roles:
        if(updateEmployeeDto.getRoles() != null && !updateEmployeeDto.getRoles().isEmpty()){
            List<Role> roles =
                    updateEmployeeDto.getRoles().stream().map(roleName-> roleRepository.findByRoleName(roleName).orElseThrow()).toList();
            employee.setRoles(roles);
        }
        // update employee balance if provided:
        if (updateEmployeeDto.getEmployeeBalance() != null){
            EmployeeBalance balance = employee.getEmployeeBalance() != null ? employee.getEmployeeBalance() : employeeBalanceRepository.findByEmployee(employee).orElseThrow();
            EmployeeBalance updatedBalance = updateBalance(balance, updateEmployeeDto.getEmployeeBalance());
            employee.setEmployeeBalance(updatedBalance);
        }
        // update employee professional details if provided:
        if (updateEmployeeDto.getProfessionalDetailsDto() != null){
            EmployeeProfessionalDetails updatedProfessionalDetails = updateProfessionalDetails(employee, updateEmployeeDto.getProfessionalDetailsDto());
            employee.setEmployeeProfessionalDetails(updatedProfessionalDetails);
        }
        // update employee social details if provided:
        if(updateEmployeeDto.getSocialDetailsDto() != null){
            EmployeeSocialDetails updatedSocialDetails = updateSocialDetails(employee, updateEmployeeDto.getSocialDetailsDto());
            employee.setEmployeeSocialDetails(updatedSocialDetails);
        }
        // update employee contact details if provided:
        if(updateEmployeeDto.getContactDetailsDto() != null){
             EmployeeContactDetails updatedContactDetails = updateContactDetails(employee, updateEmployeeDto.getContactDetailsDto());
             employee.setEmployeeContactDetails(updatedContactDetails);
        }
        // finally, save the employee:
        Employee savedEmployee = employeeRepository.save(employee);

        return new EmployeeDetailsDto(savedEmployee);
    }

    @Override
    public void updateEmployeeProfilePicture(String email, MultipartFile picture) {
        // Check if the employee exists:
        Employee employee = employeeRepository
                .findByEmail(email).orElseThrow(()-> new UserNotFoundException(email));

        // Upload the profile picture and get the URL:
        String url = employeeProfilePictureUploader.uploadProfilePicture(picture, employee.getEmployeeProfessionalDetails().getMatriculation());
        // Set the URL in employee entity and save:
        employee.setProfilePictureUrl(url);
        employeeRepository.save(employee);
    }

    private EmployeeBalance updateBalance(EmployeeBalance employeeBalance, EmployeeBalanceDto employeeBalanceDto) {
        if(employeeBalanceDto.getYear() != 0){
            employeeBalance.setYear(employeeBalanceDto.getYear());
        }
        if(employeeBalanceDto.getAnnualBalance() != 0){
            employeeBalance.setAnnualBalance(employeeBalanceDto.getAnnualBalance());
        }
        if(employeeBalanceDto.getCurrentBalance() != 0){
            employeeBalance.setCurrentBalance(employeeBalanceDto.getCurrentBalance());
        }
        if(employeeBalanceDto.getAccumulatedBalance() != 0){
            employeeBalance.setAccumulatedBalance(employeeBalanceDto.getAccumulatedBalance());
        }
        if(employeeBalanceDto.getUsedBalance() != 0){
            employeeBalance.setUsedBalance(employeeBalanceDto.getUsedBalance());
        }

        return employeeBalanceRepository.save(employeeBalance);
    }

    /**
     *
     * @param employee
     * @param dto
     * @return
     */
    private EmployeeProfessionalDetails updateProfessionalDetails(
            Employee employee , UpdateEmployeeProDetailsDto dto
    ){
        EmployeeProfessionalDetails professionalDetails = employee.getEmployeeProfessionalDetails();
        // If the employee does not have professional details, create a new instance:
        if(professionalDetails == null){
            professionalDetails = new EmployeeProfessionalDetails();
        }
        // Update Matriculation Number
        if(dto.getMatriculation() != null){
            professionalDetails.setMatriculation(dto.getMatriculation());
        }
        // Update the occupation
        if(dto.getOccupation() != null){
            professionalDetails.setOccupation(dto.getOccupation());
        }
        // Update Professional Email
        if(dto.getProfessionalEmail() != null){
            professionalDetails.setProfessionalEmail(dto.getProfessionalEmail());
        }

        // Update the professional phone number
        if(dto.getProfessionalPhoneNumber() != null){
            professionalDetails.setProfessionalPhoneNumber(dto.getProfessionalPhoneNumber());
        }
        // Update the professional fixed number
        if(dto.getProfessionalFixedPhoneNumber() != null){
            professionalDetails.setProfessionalFixedPhoneNumber(dto.getProfessionalFixedPhoneNumber());
        }
        // Update the extension
        if(dto.getExtension() != null){
            professionalDetails.setExtension(dto.getExtension());
        }
        // Update Join Date
        if(dto.getJoinDate() != null){
            professionalDetails.setJoinDate(dto.getJoinDate());
        }
        // Update the department
        if(dto.getDepartment() != null){
            professionalDetails.setDepartment(EmployeeDepartment.valueOf(dto.getDepartment()));
        }
        // Update the entity
        if(dto.getEntity() != null){
            professionalDetails.setEntity(EmployeeEntity.valueOf(dto.getEntity()));
        }
        // Update the join date
        if(dto.getJoinDate() != null){
            professionalDetails.setJoinDate(dto.getJoinDate());
        }
        // Update Manager
        if(dto.getManagerId() != null){
            Employee newManager = employeeQueryService.getManager(dto.getManagerId());
            assert newManager != null;
            professionalDetails.setManager(newManager);
        }

        return professionalDetails;
    }

    /**
     *
     * @param employee
     * @param dto
     * @return
     */
    private EmployeeSocialDetails updateSocialDetails(
            Employee employee, UpdateEmployeeSocialDetailsDto dto
    ){
        // Fetch the social details:
        EmployeeSocialDetails employeeSocialDetails = employee.getEmployeeSocialDetails();
        if(employeeSocialDetails == null){
            employeeSocialDetails = new EmployeeSocialDetails();
        }

        // Update the CNSS number
        if(dto.getCnssNumber() != null){
            employeeSocialDetails.setCnssNumber(dto.getCnssNumber());
        }
        // Update the CIMR number
        if(dto.getCimrNumber() != null){
            employeeSocialDetails.setCimrNumber(dto.getCimrNumber());
        }
        // Update the insurance number
        if(dto.getInsuranceNumber() != null){
            employeeSocialDetails.setInsuranceNumber(dto.getInsuranceNumber());
        }

        return employeeSocialDetails;
    }

    /**
     *
     * @param employee
     * @param dto
     * @return
     */
    private EmployeeContactDetails updateContactDetails(Employee employee, UpdateEmployeeContactDetailsDto dto){
        // Fetch the employee contact details
        EmployeeContactDetails employeeContactDetails = employee.getEmployeeContactDetails();
        if(employeeContactDetails == null){
            employeeContactDetails = new EmployeeContactDetails();
        }
        // Update the person to call in case of emergency
        if(dto.getPersonToCallInCaseOfEmergency() != null){
            employeeContactDetails.setPersonToContactInCaseOfEmergency(dto.getPersonToCallInCaseOfEmergency());
        }
        // Update the emergency contact number
        if(dto.getEmergencyContactNumber() != null){
            employeeContactDetails.setEmergencyContactNumber(dto.getEmergencyContactNumber());
        }

        return employeeContactDetails;
    }
}
