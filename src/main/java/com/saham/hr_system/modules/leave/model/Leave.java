package com.saham.hr_system.modules.leave.model;

import com.saham.hr_system.modules.employees.model.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "leaves", indexes = {
        @Index(name = "idx_employee_from_date", columnList = "employee_id, from_date"), @Index(name = "idx_employee_to_date", columnList = "employee_id, to_date"),
        @Index(name = "idx_reference_number", columnList = "reference_number"),
        @Index(name = "idx_employee_id", columnList = "employee_id")
})
public class Leave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leaveId;

    @Column(name = "reference_number", unique = true, nullable = false, columnDefinition = "VARCHAR(255) default ''")
    private String referenceNumber;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "from_date", nullable = false)
    private LocalDate fromDate;

    @Column(name = "leave_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private LeaveType leaveType;

    @Column(name= "to_date", nullable = false)
    private LocalDate toDate;

    @Column(name = "total_days")
    private double totalDays;
}
