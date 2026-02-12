package com.saham.hr_system.modules.employees.service.implementation;

import com.saham.hr_system.exception.UserNotFoundException;
import com.saham.hr_system.modules.employees.dto.EmployeeBalanceDto;
import com.saham.hr_system.modules.employees.dto.EmployeeDetailsDto;
import com.saham.hr_system.modules.employees.dto.EmployeeProfileDetails;
import com.saham.hr_system.modules.employees.dto.UpdateEmployeeDto;
import com.saham.hr_system.modules.employees.model.*;
import com.saham.hr_system.modules.employees.repository.EmployeeBalanceRepository;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.modules.employees.repository.RoleRepository;
import com.saham.hr_system.modules.employees.service.EmployeeUpdateService;
import com.saham.hr_system.modules.employees.utils.EmployeeProfilePictureUploader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
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
        /*
        // update the new manager:
        if(updateEmployeeDto.getManagerId() != null){
            if(!employeeQueryService.verifyManager(updateEmployeeDto.getManagerId())){
                throw new UserNotFoundException("Manager with name " + updateEmployeeDto.getManagerName() + " not found.");
            } else {
                Employee manager = employeeQueryService.getManager(updateEmployeeDto.getManagerName());
                employee.setManager(manager);
            }

        }

         */
        // update the new roles:
        if(updateEmployeeDto.getRoles() != null && !updateEmployeeDto.getRoles().isEmpty()){
            List<Role> roles =
                    updateEmployeeDto.getRoles().stream().map(roleName-> roleRepository.findByRoleName(roleName).orElseThrow()).toList();
            employee.setRoles(roles);
        }
        if (updateEmployeeDto.getEmployeeBalance() != null){
            EmployeeBalance balance = employee.getEmployeeBalance() != null ? employeeBalanceRepository.findByEmployee(employee).orElseThrow() : null;
            EmployeeBalance updatedBalance = updateBalance(balance, updateEmployeeDto.getEmployeeBalance());
            employee.setEmployeeBalance(updatedBalance);
        }
        // finally, save the employee:
        Employee savedEmployee = employeeRepository.save(employee);

        return new EmployeeDetailsDto(savedEmployee);
    }

    @Override
    public void updateEmployeeProfilePicture(Long employeeId, MultipartFile picture) {
        // Check if the employee exists:
        Employee employee = employeeRepository
                .findById(employeeId).orElseThrow(()-> new UserNotFoundException("Employee with ID " + employeeId + " not found."));

        // Upload the profile picture and get the URL:
        //String url = employeeProfilePictureUploader.uploadProfilePicture();
        // Set the URL in employee entity and save:
        //employee.setProfilePictureUrl(url);
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

    private EmployeeProfileDetails updateProfessionalDetails(){return null;}
    private EmployeeSocialDetails updateSocialDetails(){return null;}
    private EmployeeContactDetails updateContactDetails(){return null;}
}
