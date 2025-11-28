package com.saham.hr_system.unit;

import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.when;

public class EmployeeServiceUnitTest {

    @Mock
    private EmployeeRepository employeeRepository;

    private Employee employee;
    private Employee manager;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Smith");
        employee.setEmail("john.smith@gmail.com");

        // manager:
        manager = new Employee();
        manager.setId(2L);
        manager.setFirstName("Jane");
        manager.setLastName("Doe");
        manager.setEmail("jane.doe@gmail.com");

        employee.setManager(manager);
    }

    @Test
    void testFindEmployeeManager(){
        when(employeeRepository.findById(1L)).thenReturn(Optional.ofNullable(employee));
        Optional<Employee> fetchedEmployee = employeeRepository.findById(1L);
        assert fetchedEmployee.isPresent();

    }
}
