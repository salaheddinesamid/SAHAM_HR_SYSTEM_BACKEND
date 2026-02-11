package com.saham.hr_system.employee.unit;

import com.saham.hr_system.modules.employees.dto.NewEmployeeContactDetails;
import com.saham.hr_system.modules.employees.mapper.EmployeeContactDetailsMapper;
import com.saham.hr_system.modules.employees.model.EmployeeContactDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmployeeContactDetailsMapperUnitTest {

    @InjectMocks
    private EmployeeContactDetailsMapper employeeContactDetailsMapper;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testMapDtoToContactDetailsSuccess(){

        NewEmployeeContactDetails contactDetails = new NewEmployeeContactDetails(
                "Dad",
                "+212612345678"
        );

        // Act & Verify
        EmployeeContactDetails result = employeeContactDetailsMapper.mapToEmployeeContactDetails(contactDetails);
        assert result.getPersonToContactInCaseOfEmergency().equals(contactDetails.getPersonToContactInCaseOfEmergency());
        assertEquals("Dad", result.getPersonToContactInCaseOfEmergency());
        assertEquals("+212612345678", result.getEmergencyContactNumber());
    }
}
