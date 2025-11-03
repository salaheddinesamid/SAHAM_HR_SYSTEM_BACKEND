package com.saham.hr_system.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "employee_balance")
@Getter
@Setter
public class EmployeeBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long balanceId;

    @Column(name = "year")
    private int year;

    @Column(name = "initial_balance")
    private double initialBalance;

    @Column(name = "monthly_balance")
    private double monthlyBalance;

    @Column(name = "accumulated_balance")
    private double accumulatedBalance;

    @Column(name = "used_balance")
    private double usedBalance;

    @Column(name = "days_left")
    private double daysLeft;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @OneToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private Employee employee;

}
