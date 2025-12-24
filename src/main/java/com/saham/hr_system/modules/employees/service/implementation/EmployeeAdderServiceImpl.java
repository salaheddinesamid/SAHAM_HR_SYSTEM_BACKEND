package com.saham.hr_system.modules.employees.service.implementation;

import com.saham.hr_system.modules.employees.dto.EmployeeDetailsDto;
import com.saham.hr_system.modules.employees.dto.NewEmployeeDto;
import com.saham.hr_system.modules.employees.mapper.EmployeeMapper;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.model.EmployeeBalance;
import com.saham.hr_system.modules.employees.model.Role;
import com.saham.hr_system.modules.employees.repository.EmployeeBalanceRepository;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.modules.employees.repository.RoleRepository;
import com.saham.hr_system.modules.employees.service.EmployeeAdderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeAdderServiceImpl implements EmployeeAdderService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeBalanceRepository employeeBalanceRepository;
    private final RoleRepository roleRepository;
    private final EmployeeMapper employeeMapper;
    private final EmployeeQueryServiceImpl employeeQueryService;

    @Autowired
    public EmployeeAdderServiceImpl(EmployeeRepository employeeRepository, EmployeeBalanceRepository employeeBalanceRepository, RoleRepository roleRepository, EmployeeMapper employeeMapper, EmployeeQueryServiceImpl employeeQueryService) {
        this.employeeRepository = employeeRepository;
        this.employeeBalanceRepository = employeeBalanceRepository;
        this.roleRepository = roleRepository;
        this.employeeMapper = employeeMapper;
        this.employeeQueryService = employeeQueryService;
    }

    @Override
    public boolean verify() {
        return false;
    }

    @Override
    public EmployeeDetailsDto add(NewEmployeeDto newEmployeeRequestDto) {
        if(!verify()){

        }
        // check if the employee already exists:
        if(employeeRepository.existsByMatriculation(newEmployeeRequestDto.getMatriculation())) {
            throw new IllegalArgumentException("Employee with matriculation " + newEmployeeRequestDto.getMatriculation() + " already exists.");
        }
        // fetch the manager:
        Role managerRole = roleRepository.findByRoleName("MANAGER").orElseThrow();

        Employee manager =
                employeeQueryService.getManager(newEmployeeRequestDto.getManagerName());
        // create new employee:
        Employee employee = employeeMapper
                .mapToEmployee(newEmployeeRequestDto);
        // set the manager:
        employee.setManager(manager);

        // create new balance:
        EmployeeBalance employeeBalance =
                employeeMapper.mapToEmployeeBalanceDto(newEmployeeRequestDto.getEmployeeBalance());
        // save the balance:

        EmployeeBalance savedBalance =
                employeeBalanceRepository.save(employeeBalance);

        // attach the balance to the employee:
        employee.setEmployeeBalance(savedBalance);

        Employee savedEmployee = employeeRepository.save(employee);

        // notify the employee and Manager:

        return new EmployeeDetailsDto(savedEmployee);
    }
}
