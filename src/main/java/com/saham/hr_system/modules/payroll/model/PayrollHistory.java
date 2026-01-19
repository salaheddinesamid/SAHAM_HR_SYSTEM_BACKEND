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

    @Column(name = "execution_date", nullable = false)
    private LocalDateTime executionDate;

    @Column(name = "number_of_employees", nullable = false)
    private int numberOfEmployees;

    @Column(name = "number_of_payrolls", nullable = false)
    private int numberOfPayrolls;

    @Enumerated(EnumType.STRING)
    private PayrollHistoryStatus status;
}
