package com.saham.hr_system.modules.employees.mapper;

import com.saham.hr_system.modules.employees.dto.NewEmployeeContactDetails;
import com.saham.hr_system.modules.employees.model.EmployeeContactDetails;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class EmployeeContactDetailsMapper {
    /**
     *
     * @param dto
     * @return
     */
    public EmployeeContactDetails mapToEmployeeContactDetails(NewEmployeeContactDetails dto){
        EmployeeContactDetails employeeContactDetails = new EmployeeContactDetails();
        employeeContactDetails.setPersonToContactInCaseOfEmergency(dto.getPersonToContactInCaseOfEmergency());
        employeeContactDetails.setEmergencyContactNumber(dto.getEmergencyContactNumber());

        return employeeContactDetails;
    }
}
