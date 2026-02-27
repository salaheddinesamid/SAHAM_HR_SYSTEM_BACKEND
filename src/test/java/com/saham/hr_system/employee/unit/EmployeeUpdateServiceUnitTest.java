package com.saham.hr_system.employee.unit;

import com.saham.hr_system.modules.employees.dto.*;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.model.EmployeeProfessionalDetails;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.modules.employees.service.implementation.EmployeeQueryServiceImpl;
import com.saham.hr_system.modules.employees.service.implementation.EmployeeUpdateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class EmployeeUpdateServiceUnitTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeQueryServiceImpl employeeQueryService;

    @InjectMocks
    private EmployeeUpdateServiceImpl employeeUpdateService;

    private Employee employee;
    private Employee manager;
    private EmployeeProfessionalDetails managerProfessionalDetails;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        // Mock Manager
        managerProfessionalDetails = new EmployeeProfessionalDetails();
        manager = new Employee();
        manager.setId(2L);
        // Mock Employee

        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Smith");
        employee.setEmail("john.smith@saham.com");
    }

    @Test
    void testUpdateEmployeeSuccess(){
        // Given:
        UpdateEmployeeProDetailsDto proDetailsDto = new UpdateEmployeeProDetailsDto(
                "EMP2029",
                null,
                null,
                null,
                2L,
                null,
                null,
                null,
                null,
                null,
                null
        );
        UpdateEmployeeSocialDetailsDto socialDetailsDto = new UpdateEmployeeSocialDetailsDto(
                "CNSS397362",
                null,
                null
        );
        UpdateEmployeeContactDetailsDto contactDetailsDto = new UpdateEmployeeContactDetailsDto(

        );
        UpdateEmployeeDto employeeDto = new UpdateEmployeeDto(
                "AMINE",
                "Samid",
                null,
                "",
                "MARRIED",
                null,
                null,
                LocalDate.of(2003,12, 3),
                null,
                null,
                null,
                null,
                proDetailsDto,
                socialDetailsDto,
                contactDetailsDto
        );

        // Arrange:
        when(employeeRepository.findByEmployeeProfessionalDetails_Matriculation("EMP001")).thenReturn(Optional.of(employee));
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeQueryService.getManager(2L)).thenReturn(manager);
        when(employeeRepository.save(any())).thenReturn(employee);
        // Act and verify:
        EmployeeDetailsDto results = employeeUpdateService.updateEmployee(1L, employeeDto);
        verify(employeeRepository, times(1)).save(any());
        assertNotNull(results.getProfessionalDetails());
    }

    @Test
    void testUpdateEmployeeThrowEmployeeNotFound(){}

    @Test
    void testUpdateEmployeeThrowInvalidData(){}
}
