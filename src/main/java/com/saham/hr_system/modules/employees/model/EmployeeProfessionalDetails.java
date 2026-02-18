package com.saham.hr_system.modules.employees.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "employee_professional_details")
public class EmployeeProfessionalDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "matriculation", nullable = false, unique = true)
    private String matriculation;

    @Column(name = "occupation")
    private String occupation;

    @Column(name = "department")
    @Enumerated(EnumType.STRING)
    private EmployeeDepartment department;

    @Column(name = "entity")
    @Enumerated(EnumType.STRING)
    private EmployeeEntity entity;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @Column(name = "join_date")
    private LocalDate joinDate;

    @Column(name = "site")
    private String site;

    @Column(name = "professional_phone_number")
    private String professionalPhoneNumber;

    @Column(name = "professional_email")
    private String professionalEmail;

    @Column(name = "professional_fixed_phone_number")
    private String professionalFixedPhoneNumber;

    @Column(name = "extension")
    private String extension;
}
