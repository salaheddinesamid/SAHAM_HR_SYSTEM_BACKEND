package com.saham.hr_system.modules.employees.mapper;

import com.saham.hr_system.modules.employees.dto.NewEmployeeContactDetails;
import com.saham.hr_system.modules.employees.model.EmployeeContactDetails;
import org.springframework.stereotype.Component;
/**
 * Mapper for EmployeeContactDetails
 */
@Component
public class EmployeeContactDetailsMapper {

    /**
     * Maps NewEmployeeContactDetails DTO to EmployeeContactDetails entity
     * @param dto the NewEmployeeContactDetails DTO
     * @return the mapped EmployeeContactDetails entity
     */
    public EmployeeContactDetails mapToEmployeeContactDetails(NewEmployeeContactDetails dto){
        EmployeeContactDetails employeeContactDetails = new EmployeeContactDetails();
        employeeContactDetails.setPersonToContactInCaseOfEmergency(dto.getPersonToContactInCaseOfEmergency());
        employeeContactDetails.setEmergencyContactNumber(dto.getEmergencyContactNumber());

        return employeeContactDetails;
    }
}
