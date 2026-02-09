package com.saham.hr_system.modules.employees.mapper;

import com.saham.hr_system.modules.employees.dto.NewEmployeeSocialDetails;
import com.saham.hr_system.modules.employees.model.EmployeeSocialDetails;
import org.springframework.stereotype.Component;

@Component
public class EmployeeSocialDetailMapper {

    public EmployeeSocialDetails mapToEmployeeSocialDetails(NewEmployeeSocialDetails dto){
        EmployeeSocialDetails employeeSocialDetails = new EmployeeSocialDetails();
        employeeSocialDetails.setCnssNumber(dto.getCnssNumber());
        employeeSocialDetails.setCimrNumber(dto.getCimrNumber());
        employeeSocialDetails.setInsuranceNumber(dto.getInsuranceNumber());
        employeeSocialDetails.setInsuranceProvider(dto.getInsuranceProvider());

        return employeeSocialDetails;
    }
}
