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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeUpdateServiceImpl implements EmployeeUpdateService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeBalanceRepository employeeBalanceRepository;
    private final EmployeeQueryServiceImpl employeeQueryService;
    private final RoleRepository roleRepository;

    public EmployeeUpdateServiceImpl(EmployeeRepository employeeRepository, EmployeeBalanceRepository employeeBalanceRepository, EmployeeQueryServiceImpl employeeQueryService, RoleRepository roleRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeBalanceRepository = employeeBalanceRepository;
        this.employeeQueryService = employeeQueryService;
        this.roleRepository = roleRepository;
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
        // update the new manager:
        if(updateEmployeeDto.getManagerName() != null){
            if(!employeeQueryService.verifyManager(updateEmployeeDto.getManagerName())){
                throw new UserNotFoundException("Manager with name " + updateEmployeeDto.getManagerName() + " not found.");
            } else {
                Employee manager = employeeQueryService.getManager(updateEmployeeDto.getManagerName());
                employee.setManager(manager);
            }

        }
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
