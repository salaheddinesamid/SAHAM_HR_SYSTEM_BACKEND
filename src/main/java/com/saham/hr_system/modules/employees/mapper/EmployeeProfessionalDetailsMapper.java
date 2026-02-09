package com.saham.hr_system.modules.employees.mapper;

import com.saham.hr_system.modules.employees.dto.NewEmployeeProfessionalDetailsDto;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.model.EmployeeEntity;
import com.saham.hr_system.modules.employees.model.EmployeeProfessionalDetails;
import com.saham.hr_system.modules.employees.service.implementation.EmployeeQueryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeeProfessionalDetailsMapper {

    private final EmployeeQueryServiceImpl employeeQueryService;
    @Autowired
    public EmployeeProfessionalDetailsMapper(EmployeeQueryServiceImpl employeeQueryService) {
        this.employeeQueryService = employeeQueryService;
    }

    public EmployeeProfessionalDetails mapToEmployeeProfessionalDetails(NewEmployeeProfessionalDetailsDto dto){
        EmployeeProfessionalDetails employeeProfessionalDetails = new EmployeeProfessionalDetails();
        // Fetch the manager:
        Employee manager = employeeQueryService
                .getManager(dto.getManagerName());
        assert manager != null;
        employeeProfessionalDetails.setManager(manager);
        employeeProfessionalDetails.setProfessionalPhoneNumber(dto.getProfessionalPhoneNumber());
        employeeProfessionalDetails.setEntity(EmployeeEntity.valueOf(dto.getEntity()));
        employeeProfessionalDetails.setOccupation(dto.getOccupation());
        employeeProfessionalDetails.setJoinDate(dto.getJoinDate());
        employeeProfessionalDetails.setDepartment(dto.getDepartment());
        employeeProfessionalDetails.setMatriculation(dto.getMatriculation());
        employeeProfessionalDetails.setSite(dto.getSite());
        employeeProfessionalDetails.setExtension(dto.getExtension());

        return employeeProfessionalDetails;

    }
}
