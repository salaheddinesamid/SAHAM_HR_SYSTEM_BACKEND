package com.saham.hr_system.modules.employees.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "employee_social_details")
public class EmployeeSocialDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cnss_number", unique = true)
    private String cnssNumber;

    @Column(name = "cimr_number", unique = true)
    private String cimrNumber;

    @Column(name = "insurance_number", unique = true)
    private String insuranceNumber;

    @Column(name = "insurance_provider")
    private String insuranceProvider;
}
