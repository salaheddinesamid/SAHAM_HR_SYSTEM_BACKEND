package com.saham.hr_system.modules.analytics.service.implementation;

import com.saham.hr_system.modules.analytics.dto.EmployeeAnalyticsDto;
import com.saham.hr_system.modules.analytics.service.EmployeeAnalyticsService;
import com.saham.hr_system.modules.employees.model.*;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeAnalyticsServiceImpl implements EmployeeAnalyticsService {
    private final EmployeeRepository employeeRepository;
    @Autowired
    public EmployeeAnalyticsServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public EmployeeAnalyticsDto getEmployeeAnalyticsOverview(String department, String entity) {
        // Fetch all the employees in the specified department and entity
        List<Employee> employees = employeeRepository.findAll();
        List<Employee> filteredEmployees = new ArrayList<>();


        if(department.equals("ALL") || entity.equals("ALL")){
            filteredEmployees = employees;
        }
        // Filter by department
        if(!department.equals("ALL")){
            filteredEmployees =
                    employees.stream().filter(employee -> employee.getEmployeeProfessionalDetails().getDepartment().equals(EmployeeDepartment.valueOf(department)))
                            .toList();
        }
        if(!entity.equals("ALL")){
            filteredEmployees =
                    employees.stream().filter(employee -> employee.getEmployeeProfessionalDetails().getEntity().equals(EmployeeEntity.valueOf(entity)))
                            .toList();
        }


        long totalEmployees = filteredEmployees.size();
        long totalMaleEmployees  = filteredEmployees.stream()
                .filter(employee -> employee.getSex().equals(EmployeeSex.valueOf("MALE")))
                .count();
        long totalFemaleEmployees = filteredEmployees.stream()
                .filter(employee -> employee.getSex().equals(EmployeeSex.valueOf("FEMALE")))
                .count();
        long totalActiveEmployees = filteredEmployees.stream()
                .filter(employee -> employee.getStatus().equals(EmployeeStatus.valueOf("AVAILABLE")))
                .count();
        long totalInactiveEmployees = totalEmployees - totalActiveEmployees;

        return new EmployeeAnalyticsDto(
                totalEmployees,
                totalActiveEmployees,
                totalInactiveEmployees,
                totalMaleEmployees,
                totalFemaleEmployees
        );
    }
}
