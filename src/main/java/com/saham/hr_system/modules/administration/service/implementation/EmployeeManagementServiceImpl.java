package com.saham.hr_system.modules.administration.service.implementation;

import com.saham.hr_system.exception.UserNotFoundException;
import com.saham.hr_system.modules.administration.dto.EmployeeDetailsResponseDto;
import com.saham.hr_system.modules.administration.dto.NewEmployeeRequestDto;
import com.saham.hr_system.modules.administration.mapper.EmployeeMapper;
import com.saham.hr_system.modules.administration.service.EmployeeManagementService;
import com.saham.hr_system.modules.employees.dto.EmployeeDetailsDto;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.model.EmployeeBalance;
import com.saham.hr_system.modules.employees.model.Role;
import com.saham.hr_system.modules.employees.model.RoleName;
import com.saham.hr_system.modules.employees.repository.EmployeeBalanceRepository;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.modules.employees.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeManagementServiceImpl implements EmployeeManagementService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeBalanceRepository employeeBalanceRepository;
    private final EmployeeQueryServiceImpl employeeQueryService;
    private final RoleRepository roleRepository;
    private final EmployeeMapper employeeMapper;

    @Autowired
    public EmployeeManagementServiceImpl(EmployeeRepository employeeRepository, EmployeeBalanceRepository employeeBalanceRepository, EmployeeQueryServiceImpl employeeQueryService, RoleRepository roleRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeBalanceRepository = employeeBalanceRepository;
        this.employeeQueryService = employeeQueryService;
        this.roleRepository = roleRepository;
        this.employeeMapper = employeeMapper;
    }

    @Override
    @Transactional
    public EmployeeDetailsResponseDto newEmployee(NewEmployeeRequestDto requestDto) {
        // check if the employee already exists:
        if(employeeRepository.existsByMatriculation(requestDto.getMatriculation())) {
            throw new IllegalArgumentException("Employee with matriculation " + requestDto.getMatriculation() + " already exists.");
        }
        // fetch the manager:
        Role managerRole = roleRepository.findByRoleName("MANAGER").orElseThrow();

        Employee manager =
                employeeQueryService.getManager(requestDto.getManagerName());
        // create new employee:
        Employee employee = employeeMapper
                .mapToEmployee(requestDto);
        // set the manager:
        employee.setManager(manager);

        // create new balance:
        EmployeeBalance employeeBalance =
                employeeMapper.mapToEmployeeBalanceDto(requestDto.getEmployeeBalance());
        // save the balance:

        EmployeeBalance savedBalance =
                employeeBalanceRepository.save(employeeBalance);

        // attach the balance to the employee:
        employee.setEmployeeBalance(savedBalance);

        Employee savedEmployee = employeeRepository.save(employee);

        return new EmployeeDetailsResponseDto(savedEmployee);
    }

    @Override
    public EmployeeDetailsDto modifyEmployee(String matriculation) {
        return null;
    }

    @Override
    public void offBoardEmployee(String matriculation) {
        // fetch the employee from the db:
        Employee employee =
                employeeRepository.findByMatriculation(matriculation).orElseThrow(()-> new UserNotFoundException(matriculation));
    }
}
