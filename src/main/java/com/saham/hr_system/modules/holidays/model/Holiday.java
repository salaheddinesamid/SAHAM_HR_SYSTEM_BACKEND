package com.saham.hr_system.modules.holidays.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "holidays")
public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "leave_days")
    private int leaveDays;

    @Column(name = "last_update")
    private LocalDateTime lastUpdate;
}
