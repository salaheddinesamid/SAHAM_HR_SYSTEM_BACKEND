package com.saham.hr_system.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "managers")
public class Manager extends Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long managerId;

    @Column(name = "department", nullable = true)
    private String department;

    @OneToMany
    @JoinColumn(name = "employee_id")
    private List<Employee> subordinates;

}
