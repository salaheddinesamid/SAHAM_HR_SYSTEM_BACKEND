package com.saham.hr_system.modules.employees.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeProfileDetails {
}

@Data
class PersonalDetails{
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String nationality;
    private String ID;
    private String familySituation;
}
@Data
class ProfessionalDetails{
    private String matriculation;
    private String occupation;
    private LocalDate joinDate;
    private String department;
    private String managerFullName;
    private String site;
    private String professionalPhoneNumber;
    private String fixPhoneNumber;
    private String extension;
    private String professionalEmail;
}
@Data
class SocialDetails{}
@Data
class ContactDetails{}
