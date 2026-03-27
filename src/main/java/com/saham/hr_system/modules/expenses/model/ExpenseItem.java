package com.saham.hr_system.modules.expenses.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "expense_items")
public class ExpenseItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String designation;

    @Column(nullable = false)
    private double amount;

    @Column(name = "expense_date", nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_id", nullable = false)
    private Expense expense;

    @Column(name = "invoiced", nullable = false)
    private boolean invoiced;
    /**
     * The designation of the expense item, which can be one of the following:
     * - TRAVEL: for travel expenses
     * - MEAL: for meal expenses
     * - ACCOMMODATION: for accommodation expenses
     * - OTHER: for other types of expenses
     */

    //@Column(name = "designation")
    //@Enumerated(EnumType.STRING)
    //private ExpenseDesignation designation;
}
