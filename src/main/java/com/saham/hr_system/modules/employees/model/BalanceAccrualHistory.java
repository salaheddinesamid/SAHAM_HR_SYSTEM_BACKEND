package com.saham.hr_system.modules.employees.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "balance_accrual_history", indexes = {
        @Index(name = "year", columnList = "year"),
        @Index(name = "type", columnList = "type")
})
public class BalanceAccrualHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private BalanceAccrualType balanceAccrualType;

    @Column(name = "execution_date")
    private LocalDateTime executionDate;

    @Column(name = "year")
    private int year;

    @Column(name = "total_operations")
    private double totalOperations;
}
