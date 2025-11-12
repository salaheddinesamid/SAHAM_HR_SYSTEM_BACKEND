package com.saham.hr_system.modules.expenses.model;

import com.saham.hr_system.modules.employees.model.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
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
    private LocalDate issueDate;

    @Column(name = "total_amount")
    private double totalAmount;

    @Column(name = "balance")
    private double balance;

    @OneToMany
    @JoinColumn(name = "items")
    private List<ExpenseItem> items;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
