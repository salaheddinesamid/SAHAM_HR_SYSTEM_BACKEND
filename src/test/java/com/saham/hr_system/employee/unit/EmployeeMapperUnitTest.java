package com.saham.hr_system.employee.unit;

import com.saham.hr_system.modules.employees.mapper.EmployeeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

public class EmployeeMapperUnitTest {

    @InjectMocks
    private EmployeeMapper employeeMapper;

    @BeforeEach
    void setUpe(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testMapDtoToEmployeeSuccess(){}
}
