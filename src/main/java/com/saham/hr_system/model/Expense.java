package com.saham.hr_system.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "issue_date")
    private LocalDateTime issueDate;

    @Column(name = "total_amount")
    private double totalAmount;

    @Column(name = "balance")
    private double balance;

    @OneToMany
    @JoinColumn(name = "items")
    private List<ExpenseItem> items;
}
