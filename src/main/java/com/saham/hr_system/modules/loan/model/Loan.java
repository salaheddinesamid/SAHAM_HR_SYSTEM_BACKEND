package com.saham.hr_system.modules.loan.model;

import com.saham.hr_system.modules.employees.model.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "loans")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanId;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "issueDate")
    private LocalDateTime issueDate;

    @Column(name = "amount")
    private double amount;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private LoanType type;

    @Column(name = "approval_date")
    private LocalDateTime approvalDate;

}
