package com.saham.hr_system.modules.employees.model;

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
    private int year; // the year of the balance

    @Column(name = "initial_balance")
    private double initialBalance; // the balance of the year

    @Column(name = "monthly_balance")
    private double monthlyBalance; // the monthly balance

    @Column(name = "accumulated_balance")
    private double accumulatedBalance; // the accumulated balance

    @Column(name = "used_balance")
    private double usedBalance; // the total days used

    @Column(name = "days_left")
    private double daysLeft; // the days left

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @OneToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private Employee employee;

}
