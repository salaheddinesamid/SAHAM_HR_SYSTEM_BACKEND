package com.saham.hr_system.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "entity")
    private String entity;

    @Column(name = "occupation",nullable = true)
    private String occupation;


    @Column(name = "password")
    private String password;

    @Column(name = "matriculation", nullable = false)
    private String matriculation;

    @Column(name = "join_date")
    private LocalDate joinDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "employee_roles",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "managed_by")
    private Employee manager;
}
