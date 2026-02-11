package com.saham.hr_system.modules.employees.service.implementation;

import com.saham.hr_system.modules.employees.dto.EmployeeDetailsDto;
import com.saham.hr_system.modules.employees.dto.NewEmployeeDto;
import com.saham.hr_system.modules.employees.mapper.EmployeeContactDetailsMapper;
import com.saham.hr_system.modules.employees.mapper.EmployeeMapper;
import com.saham.hr_system.modules.employees.mapper.EmployeeProfessionalDetailsMapper;
import com.saham.hr_system.modules.employees.mapper.EmployeeSocialDetailMapper;
import com.saham.hr_system.modules.employees.model.*;
import com.saham.hr_system.modules.employees.repository.*;
import com.saham.hr_system.modules.employees.service.EmployeeAdderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
@Service
@RequiredArgsConstructor
@Slf4j
public class CeoAdderServiceImpl implements EmployeeAdderService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final EmployeeProfessionalDetailsMapper employeeProfessionalDetailsMapper;
    private final EmployeeSocialDetailMapper employeeSocialDetailMapper;
    private final EmployeeContactDetailsMapper employeeContactDetailsMapper;
    private final EmployeeBalanceRepository employeeBalanceRepository;

    @Override
    public boolean verify() {
        return false;
    }

    @Transactional
    @Override
    public EmployeeDetailsDto add(NewEmployeeDto dto) {

        String matriculation = dto.getProfessionalDetailsDto().getMatriculation();
        if (employeeRepository.existsByEmployeeProfessionalDetails_Matriculation(matriculation)) {
            throw new IllegalArgumentException("Employee with matriculation " + matriculation + " already exists");
        }

        Map<String, Object> result = employeeMapper.mapToEmployee(dto);
        Employee employee = result.get("mappedEmployee") != null ? (Employee) result.get("mappedEmployee") : null;

        if (employee == null) {
            throw new IllegalStateException("Employee mapping failed");
        }

        employee.setEmployeeProfessionalDetails(
                employeeProfessionalDetailsMapper.mapToEmployeeProfessionalDetails(
                        dto.getProfessionalDetailsDto(), true
                )
        );

        employee.setEmployeeSocialDetails(
                employeeSocialDetailMapper.mapToEmployeeSocialDetails(dto.getEmployeeSocialDetailsDto())
        );

        employee.setEmployeeContactDetails(
                employeeContactDetailsMapper.mapToEmployeeContactDetails(dto.getEmployeeContactDetailsDto())
        );

        EmployeeBalance savedBalance = employeeBalanceRepository.save(employeeMapper.mapToEmployeeBalanceDto(dto.getEmployeeBalance()));

        employee.setEmployeeBalance(
                savedBalance
        );

        Employee savedEmployee = employeeRepository.save(employee);

        log.info("Employee created successfully with id {}", savedEmployee.getId());

        return new EmployeeDetailsDto(savedEmployee);
    }

}
