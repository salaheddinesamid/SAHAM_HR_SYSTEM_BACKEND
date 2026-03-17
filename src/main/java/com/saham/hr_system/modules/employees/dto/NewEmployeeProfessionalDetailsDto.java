package com.saham.hr_system.modules.employees.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
/** * DTO for capturing the professional details of a new employee during the onboarding process.
 */
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
