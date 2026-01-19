package com.saham.hr_system.modules.payroll.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "payrolls_history")
public class PayrollHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;

    @Column(name = "payroll_month", nullable = false)
    private int payrollMonth;

    @Column(name = "payroll_year", nullable = false)
    private int payrollYear;

    @Column(name = "execution_date", nullable = false)
    private LocalDateTime executionDate;

    @Column(name = "number_of_employees", nullable = false)
    private int numberOfEmployees;

    @Column(name = "succeeded_payrolls", nullable = false)
    private int succeededPayrolls;

    @Column(name = "failed_payrolls", nullable = false)
    private int failedPayrolls;

    @Column(name = "total_payrolls", nullable = false)
    private int totalPayrolls;

    @Enumerated(EnumType.STRING)
    private PayrollHistoryStatus status;
}
