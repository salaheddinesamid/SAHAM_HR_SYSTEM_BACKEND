package com.saham.hr_system.employee.unit;

import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.modules.employees.service.implementation.EmployeeAdderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class EmployeeAdderUnitTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeAdderServiceImpl employeeAdderService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }
}
