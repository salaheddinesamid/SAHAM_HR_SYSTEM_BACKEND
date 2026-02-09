package com.saham.hr_system.modules.employees.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class NewEmployeeProfessionalDetailsDto {
    private String matriculation;
    private String occupation;
    private String department;
    private String entity;
    private String managerName;
    private LocalDate joinDate;
    private String site;
    private String professionalPhoneNumber;
    private String professionalEmail;
    private String professionalFixedPhoneNumber;
    private String extension;
}
