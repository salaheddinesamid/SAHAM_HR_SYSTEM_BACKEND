package com.saham.hr_system.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "leave_requests")
@Getter
@Setter
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leaveRequestId;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "total_days", nullable = true)
    private double totalDays;

    @Column(name = "request_date", nullable = false)
    private LocalDateTime requestDate;

    @Column(name= "type_of_leave", nullable = false)
    private String typeOfLeave;

    @Column(name = "status", nullable = true)
    @Enumerated(EnumType.STRING)
    private LeaveRequestStatus status;
}
