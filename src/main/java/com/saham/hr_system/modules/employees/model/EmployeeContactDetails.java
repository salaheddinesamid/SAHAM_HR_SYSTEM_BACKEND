package com.saham.hr_system.modules.employees.model;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "employee_contact_details")
public class EmployeeContactDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "person_to_contact_in_case_of_emergency")
    private String personToContactInCaseOfEmergency;

    @Column(name = "emergency_contact_number")
    private String emergencyContactNumber;
}
