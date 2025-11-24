package com.saham.hr_system.modules.absence.model;

import com.saham.hr_system.modules.employees.model.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class AbsenceRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long absenceRequestId;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "total_days")
    private double totalDays;

    @Column(name = "issue_date")
    private LocalDateTime issueDate;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private AbsenceType type;

    @Column(name = "approved_by_manager")
    private boolean approvedByManager;

    @Column(name = "approved_by_hr")
    private boolean approvedByHr;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AbsenceRequestStatus status;
}
