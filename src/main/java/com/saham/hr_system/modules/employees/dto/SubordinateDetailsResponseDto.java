package com.saham.hr_system.modules.employees.dto;

import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.leave.model.Leave;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class SubordinateDetailsResponseDto {
    private Long employeeId;
    private String fullName;
    private String email;
    private String entity;
    private String occupation;
    private String matriculation;
    private LocalDate joinDate;
    private List<Leave> leaves;
    private String status;

    public SubordinateDetailsResponseDto(
            Employee employee
    ){
        this.employeeId = employee.getId();
        this.email = employee.getEmail();
        this.fullName = employee.getFullName();
        this.entity = employee.getEntity();
        this.occupation = employee.getOccupation();
        this.matriculation = employee.getMatriculation();
        this.joinDate = employee.getJoinDate();
        this.status = employee.getStatus().toString();
        this.leaves = employee.getLeaves();
    }
}
