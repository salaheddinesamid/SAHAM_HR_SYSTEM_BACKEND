package com.saham.hr_system.modules.employees.mapper;

import com.saham.hr_system.modules.employees.dto.NewEmployeeSocialDetails;
import com.saham.hr_system.modules.employees.model.EmployeeSocialDetails;
import org.springframework.stereotype.Component;
/** * Mapper class to convert NewEmployeeSocialDetails DTO to EmployeeSocialDetails entity.
 */
@Component
public class EmployeeSocialDetailMapper {

    /**
     * Maps a NewEmployeeSocialDetails DTO to an EmployeeSocialDetails entity.
     *
     * @param dto the NewEmployeeSocialDetails DTO containing the social details information
     * @return an EmployeeSocialDetails entity populated with the data from the DTO
     */
    public EmployeeSocialDetails mapToEmployeeSocialDetails(NewEmployeeSocialDetails dto){
        EmployeeSocialDetails employeeSocialDetails = new EmployeeSocialDetails();
        employeeSocialDetails.setCnssNumber(dto.getCnssNumber());
        employeeSocialDetails.setCimrNumber(dto.getCimrNumber());
        employeeSocialDetails.setInsuranceNumber(dto.getInsuranceNumber());
        employeeSocialDetails.setInsuranceProvider(dto.getInsuranceProvider());

        return employeeSocialDetails;
    }
}
