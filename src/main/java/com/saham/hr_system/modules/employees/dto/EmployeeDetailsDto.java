package com.saham.hr_system.modules.employees.dto;

import com.saham.hr_system.modules.employees.model.*;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class EmployeeDetailsDto {
    private long employeeId;
    private String fullName;
    private String firstName;
    private String lastName;
    private String sex;
    private LocalDate birthDate;
    private String email;
    private String familyStatus;
    private Integer numberOfChildren;
    private String CIN;
    private String address;
    private String profilePictureUrl;
    private ProfessionalDetailsDto professionalDetails;
    private SocialDetailsDto socialDetails;
    private ContactDetailsDto contactDetails;
    private BalanceDetails balanceDetails;
    private List<String> roles;

    public EmployeeDetailsDto(Employee employee, EmployeeBalance balance) {
        this.employeeId = employee.getId();
        this.fullName = String.format("%s %s", employee.getFirstName(), employee.getLastName());
        this.firstName = employee.getFirstName();
        this.lastName = employee.getLastName();
        this.sex = employee.getSex().toString();
        this.email = employee.getEmail();
        this.address = employee.getAddress();
        this.birthDate = employee.getBirthDate();
        this.familyStatus = employee.getFamilyStatus() != null ? employee.getFamilyStatus().toString() : null;
        this.CIN = employee.getCIN();
        this.numberOfChildren = employee.getNumberOfChildren();
        this.profilePictureUrl = employee.getProfilePictureUrl();
        this.professionalDetails = employee.getEmployeeProfessionalDetails() != null ? new ProfessionalDetailsDto(employee.getEmployeeProfessionalDetails()) : null;
        this.socialDetails = employee.getEmployeeSocialDetails() != null ? new SocialDetailsDto(employee.getEmployeeSocialDetails()) : null;
        this.contactDetails = employee.getEmployeeContactDetails() != null ? new ContactDetailsDto(employee.getEmployeeContactDetails()) : null;
        this.balanceDetails = employee.getEmployeeBalance() != null ? new BalanceDetails(employee.getEmployeeBalance()) : null;
        this.roles = employee.getRoles().stream().map(Role::getRoleName).toList();
    }
    public EmployeeDetailsDto(Employee employee) {
        this.fullName = String.format("%s %s", employee.getFirstName(), employee.getLastName());
        this.email = employee.getEmail();
        this.professionalDetails = new ProfessionalDetailsDto(employee.getEmployeeProfessionalDetails());
    }

}

@Data
class BalanceDetails{
    int year; // the year for which the balance is applicable
    double annualBalance; //
    double monthlyBalance;
    double currentBalance;
    double accumulatedBalance;
    double usedBalance;
    double reminderBalance;
    LocalDateTime lastUpdated;

    public BalanceDetails(
            EmployeeBalance employeeBalance
    ) {
        this.year = employeeBalance.getYear();
        this.annualBalance = employeeBalance.getAnnualBalance();
        this.monthlyBalance = employeeBalance.getMonthlyBalance();
        this.accumulatedBalance = employeeBalance.getAccumulatedBalance();
        this.usedBalance = employeeBalance.getUsedBalance();
        this.currentBalance = employeeBalance.getCurrentBalance();
        this.reminderBalance = employeeBalance.getRemainderBalance();
        this.lastUpdated = employeeBalance.getLastUpdated();
    }
}

@Data
class ProfessionalDetailsDto{
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

    public ProfessionalDetailsDto(EmployeeProfessionalDetails professionalDetails){
        this.matriculation = professionalDetails.getMatriculation();
        this.occupation = professionalDetails.getOccupation();
        this.department = professionalDetails.getDepartment() != null ? professionalDetails.getDepartment().toString() : "";
        this.entity = professionalDetails.getEntity() != null ? professionalDetails.getEntity().toString() : "";
        this.managerName = professionalDetails.getManager() != null ?
                String.format("%s %s", professionalDetails.getManager().getFirstName(), professionalDetails.getManager().getLastName())
                : null;
        this.joinDate = professionalDetails.getJoinDate();
        this.site = professionalDetails.getSite();
        this.professionalPhoneNumber = professionalDetails.getProfessionalPhoneNumber();
        this.professionalEmail = professionalDetails.getProfessionalEmail();
        this.professionalFixedPhoneNumber = professionalDetails.getProfessionalFixedPhoneNumber();
        this.extension = professionalDetails.getExtension();

    }
}

@Data
class SocialDetailsDto{
    private String cnssNumber;
    private String cimrNumber;
    private String insuranceNumber;
    private String insuranceProvider;

    public SocialDetailsDto(
            EmployeeSocialDetails socialDetails
    ){
        this.cnssNumber = socialDetails.getCnssNumber();
        this.cimrNumber = socialDetails.getCimrNumber();
        this.insuranceNumber = socialDetails.getInsuranceNumber();
        this.insuranceProvider = socialDetails.getInsuranceProvider();
    }
}

@Data
class ContactDetailsDto{
    private String personToContactInCaseOfEmergency;
    private String emergencyContactPhoneNumber;

    public ContactDetailsDto(EmployeeContactDetails contactDetails){
        this.personToContactInCaseOfEmergency = contactDetails.getPersonToContactInCaseOfEmergency();
        this.emergencyContactPhoneNumber = contactDetails.getEmergencyContactNumber();
    }
}


