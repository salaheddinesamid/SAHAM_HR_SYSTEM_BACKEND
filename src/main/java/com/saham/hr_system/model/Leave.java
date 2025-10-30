package com.saham.hr_system.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "leaves")
public class Leave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leaveId;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "from_date", nullable = false)
    private LocalDate fromDate;

    @Column(name= "to_date", nullable = false)
    private LocalDate toDate;
}
