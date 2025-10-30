package com.saham.hr_system.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "employees")
@Getter
@Setter
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

    @Column(name = "matriculation", nullable = false)
    private String matriculation;

    @Column(name = "join_date")
    private LocalDate joinDate;

    @OneToMany
    private List<Role> roles;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "manager_id")
    private Manager manager;
}
