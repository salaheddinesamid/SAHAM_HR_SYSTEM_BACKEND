package com.saham.hr_system.modules.employees.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewEmployeeProfessionalDetailsDto {
    @NotNull
    private String matriculation;
    private String occupation;
    private String department;
    private String entity;
    private Long managerId;
    private LocalDate joinDate;
    private String site;
    private String professionalPhoneNumber;
    private String professionalEmail;
    private String professionalFixedPhoneNumber;
    private String extension;
}
