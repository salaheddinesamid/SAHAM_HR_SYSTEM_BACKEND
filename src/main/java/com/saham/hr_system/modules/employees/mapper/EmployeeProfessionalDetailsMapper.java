package com.saham.hr_system.modules.employees.mapper;

import com.saham.hr_system.modules.employees.dto.NewEmployeeProfessionalDetailsDto;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.model.EmployeeDepartment;
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

    public EmployeeProfessionalDetails mapToEmployeeProfessionalDetails(NewEmployeeProfessionalDetailsDto dto, boolean isCeo){
        EmployeeProfessionalDetails employeeProfessionalDetails = new EmployeeProfessionalDetails();


        if(isCeo){
            employeeProfessionalDetails.setManager(null);
            employeeProfessionalDetails.setProfessionalPhoneNumber(dto.getProfessionalPhoneNumber());
            employeeProfessionalDetails.setEntity(EmployeeEntity.valueOf(dto.getEntity()));
            employeeProfessionalDetails.setOccupation(dto.getOccupation());
            employeeProfessionalDetails.setJoinDate(dto.getJoinDate());
            employeeProfessionalDetails.setDepartment(EmployeeDepartment.valueOf(dto.getDepartment()));
            employeeProfessionalDetails.setMatriculation(dto.getMatriculation());
            employeeProfessionalDetails.setSite(dto.getSite());
            employeeProfessionalDetails.setProfessionalEmail(dto.getProfessionalEmail());
            employeeProfessionalDetails.setProfessionalFixedPhoneNumber(dto.getProfessionalFixedPhoneNumber());
            employeeProfessionalDetails.setExtension(dto.getExtension());
        }
        else {
            // Fetch the manager:
            Employee manager = employeeQueryService
                    .getManager(dto.getManagerId());
            assert manager != null;
            employeeProfessionalDetails.setManager(manager);
            employeeProfessionalDetails.setProfessionalPhoneNumber(dto.getProfessionalPhoneNumber());
            employeeProfessionalDetails.setEntity(EmployeeEntity.valueOf(dto.getEntity()));
            employeeProfessionalDetails.setOccupation(dto.getOccupation());
            employeeProfessionalDetails.setJoinDate(dto.getJoinDate());
            employeeProfessionalDetails.setDepartment(EmployeeDepartment.valueOf(dto.getDepartment()));
            employeeProfessionalDetails.setMatriculation(dto.getMatriculation());
            employeeProfessionalDetails.setSite(dto.getSite());
            employeeProfessionalDetails.setProfessionalEmail(dto.getProfessionalEmail());
            employeeProfessionalDetails.setProfessionalFixedPhoneNumber(dto.getProfessionalFixedPhoneNumber());
            employeeProfessionalDetails.setExtension(dto.getExtension());
        }
        return employeeProfessionalDetails;

    }
}
