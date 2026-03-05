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

    @Column(name = "annual_balance")
    private double annualBalance; // the annual right

    @Column(name = "monthly_balance")
    private double monthlyBalance = this.annualBalance / 12; // the monthly balance

    @Column(name = "accumulated_balance")
    private double accumulatedBalance; // the accumulated balance

    @Column(name = "used_balance")
    private double usedBalance; // the total days used

    @Column(name = "remainder_balance")
    private double remainderBalance; // the remaining balance

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @Column(name = "previous_year_balance", columnDefinition = "double default 0")
    private double previousYearBalance; // the balance carried over from the previous year

    @OneToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private Employee employee;

    /**
     * Calculate the remainder balance before persisting
     * Update the last updated timestamp
     */
    @PrePersist
    void prePersist() {
        this.monthlyBalance = this.annualBalance / 12;
        this.remainderBalance = this.accumulatedBalance - this.usedBalance + this.previousYearBalance;
        this.lastUpdated = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        this.remainderBalance = this.accumulatedBalance - this.usedBalance + this.previousYearBalance;
        this.lastUpdated = LocalDateTime.now();
    }

}
