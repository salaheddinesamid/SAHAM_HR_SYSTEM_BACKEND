package com.saham.hr_system.analytics.unit;

import com.saham.hr_system.modules.analytics.dto.EmployeeAnalyticsDto;
import com.saham.hr_system.modules.analytics.service.implementation.EmployeeAnalyticsServiceImpl;
import com.saham.hr_system.modules.employees.model.*;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 *
 */
public class EmployeeAnalyticsUnitTest {

    @InjectMocks
    private EmployeeAnalyticsServiceImpl employeeAnalyticsService;

    @Mock
    private EmployeeRepository employeeRepository;

    private Employee emp1;
    private Employee emp2;
    private Employee emp3;
    private Employee emp4;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        // Init the employees:
        emp1 = new Employee();
        emp2 = new Employee();
        emp3 = new Employee();
        emp4 = new Employee();

        // Init emp1:
        emp1.setId(1L);
        emp1.setSex(EmployeeSex.MALE);
        emp1.setStatus(EmployeeStatus.AVAILABLE);

        EmployeeProfessionalDetails emp1ProfessionalDetails = new EmployeeProfessionalDetails();
        emp1ProfessionalDetails.setDepartment(EmployeeDepartment.FINANCE_DEPARTMENT);
        emp1ProfessionalDetails.setEntity(EmployeeEntity.SAHAM_HORIZON);
        emp1.setEmployeeProfessionalDetails(emp1ProfessionalDetails);

        // Init emp2:
        emp2 = new Employee();
        emp2.setId(2L);
        emp2.setSex(EmployeeSex.FEMALE);
        emp2.setStatus(EmployeeStatus.AVAILABLE);

        EmployeeProfessionalDetails emp2ProfessionalDetails = new EmployeeProfessionalDetails();
        emp2ProfessionalDetails.setId(10L);
        emp2ProfessionalDetails.setDepartment(EmployeeDepartment.IT);
        emp2ProfessionalDetails.setEntity(EmployeeEntity.SAHAM_FINANCES);
        emp2.setEmployeeProfessionalDetails(emp2ProfessionalDetails);

        // Init emp3:
        emp3 = new Employee();
        emp3.setId(4L);
        emp3.setSex(EmployeeSex.FEMALE);
        emp3.setStatus(EmployeeStatus.AVAILABLE);

        EmployeeProfessionalDetails emp3ProfessionalDetails = new EmployeeProfessionalDetails();
        emp3ProfessionalDetails.setDepartment(EmployeeDepartment.LEGAL_DEPARTMENT);
        emp3ProfessionalDetails.setEntity(EmployeeEntity.SAHAM_FOUNDATION);
        emp3.setEmployeeProfessionalDetails(emp3ProfessionalDetails);

        // Init emp4:
        emp4 = new Employee();
        emp4.setId(5L);
        emp4.setSex(EmployeeSex.FEMALE);
        emp4.setStatus(EmployeeStatus.BUSY);

        EmployeeProfessionalDetails emp4ProfessionalDetails = new EmployeeProfessionalDetails();
        emp4ProfessionalDetails.setDepartment(EmployeeDepartment.LEGAL_DEPARTMENT);
        emp4ProfessionalDetails.setEntity(EmployeeEntity.SAHAM_FOUNDATION);
        emp4.setEmployeeProfessionalDetails(emp4ProfessionalDetails);
    }

    @Test
    void testEmployeeAnalyticsOverviewSuccess(){

        // Arrange:
        when(employeeRepository.findAll()).thenReturn(List.of(emp1, emp2, emp3));
        // Act and verify:
        EmployeeAnalyticsDto results = employeeAnalyticsService.getEmployeeAnalyticsOverview("ALL", "ALL");
        // Assert
        assertEquals(3, results.getTotalEmployees());
    }

    @Test
    void testEmployeeAnalyticsOverviewByDepartment(){
        // Arrange:
        when(employeeRepository.findAll()).thenReturn(List.of(emp1, emp2, emp3));
        // Act and verify:
        EmployeeAnalyticsDto results = employeeAnalyticsService.getEmployeeAnalyticsOverview("LEGAL_DEPARTMENT", "ALL");
        // Assert
        assertEquals(1, results.getTotalEmployees());
        assertEquals(1, results.getTotalFemaleEmployees());
        assertEquals(0, results.getTotalMaleEmployees());
    }

    @Test
    void testEmployeeAnalyticsOverviewByEntity(){}

    @Test
    void testEmployeeAnalyticsOverviewFailure(){}
}
