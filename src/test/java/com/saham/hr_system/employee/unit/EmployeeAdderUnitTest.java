package com.saham.hr_system.employee.unit;

import com.saham.hr_system.modules.employees.mapper.EmployeeContactDetailsMapper;
import com.saham.hr_system.modules.employees.mapper.EmployeeMapper;
import com.saham.hr_system.modules.employees.mapper.EmployeeProfessionalDetailsMapper;
import com.saham.hr_system.modules.employees.mapper.EmployeeSocialDetailMapper;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.modules.employees.service.implementation.EmployeeAdderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class EmployeeAdderUnitTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private EmployeeProfessionalDetailsMapper employeeProfessionalDetailsMapper;

    @Mock
    private EmployeeSocialDetailMapper employeeSocialDetailMapper;

    @Mock
    private EmployeeContactDetailsMapper employeeContactDetailsMapper;

    @InjectMocks
    private EmployeeAdderServiceImpl employeeAdderService;

    Employee existingEmployee = new Employee();

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        existingEmployee.setId(2L);
        existingEmployee.getEmployeeProfessionalDetails().setMatriculation("MAT123456");
    }

    @Test
    void testAddNewEmployeeSuccess(){
        // TODO: implement the test for adding a new employee successfully
    }

    @Test
    void testAddNewEmployeeDThrowDuplicate(){
        // TODO: implement the test for adding a new employee with a duplicate matriculation number, which should throw an exception
    }
}
