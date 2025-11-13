package com.saham.hr_system.modules.loan.model;


import com.saham.hr_system.modules.employees.model.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "loan_requests")
public class LoanRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "amount")
    private double amount;

    @Column(name = "issue_date")
    private LocalDateTime issueDate;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private LoanType type;

    @Column(name = "approved_by_hr_department")
    private boolean approvedByHrDepartment;

    @Column(name = "approved_by_finance_department")
    private boolean approvedByFinanceDepartment;

    @Column(name = "motif")
    private String motif;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private LoanRequestStatus status;

    // Automatically approve the request if both departments have approved it
    @PrePersist
    void prePersist(){
        if(this.approvedByHrDepartment && this.approvedByFinanceDepartment){
            this.status = LoanRequestStatus.APPROVED;
        }
    }


}
