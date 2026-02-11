package com.saham.hr_system.employee.unit;

import com.saham.hr_system.modules.employees.dto.NewEmployeeContactDetails;
import com.saham.hr_system.modules.employees.dto.NewEmployeeSocialDetails;
import com.saham.hr_system.modules.employees.mapper.EmployeeContactDetailsMapper;
import com.saham.hr_system.modules.employees.mapper.EmployeeSocialDetailMapper;
import com.saham.hr_system.modules.employees.model.EmployeeContactDetails;
import com.saham.hr_system.modules.employees.model.EmployeeSocialDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmployeeSocialDetailsMapperUnitTest {

    @InjectMocks
    private EmployeeSocialDetailMapper employeeSocialDetailMapper;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testMapToSocialDetailsSuccess(){
        NewEmployeeSocialDetails socialDetailsDto = new NewEmployeeSocialDetails(
                "CNSS123456",
                "CIMR123456",
                "INS123456",
                ""
        );

        EmployeeSocialDetails employeeSocialDetails = employeeSocialDetailMapper.mapToEmployeeSocialDetails(socialDetailsDto);

        assertEquals(socialDetailsDto.getCnssNumber(), employeeSocialDetails.getCnssNumber());
        assertEquals(socialDetailsDto.getCimrNumber(), employeeSocialDetails.getCimrNumber());
        assertEquals(socialDetailsDto.getInsuranceNumber(), employeeSocialDetails.getInsuranceNumber());
    }

}
