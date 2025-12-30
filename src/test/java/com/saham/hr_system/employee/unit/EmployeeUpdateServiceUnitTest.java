package com.saham.hr_system.employee.unit;

import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.modules.employees.service.implementation.EmployeeUpdateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class EmployeeUpdateServiceUnitTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeUpdateServiceImpl employeeUpdateService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateEmployeeSuccess(){}

    @Test
    void testUpdateEmployeeThrowEmployeeNotFound(){}

    @Test
    void testUpdateEmployeeThrowInvalidData(){}
}
