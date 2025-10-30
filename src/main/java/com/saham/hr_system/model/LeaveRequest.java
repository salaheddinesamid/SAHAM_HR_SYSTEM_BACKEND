package com.saham.hr_system.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "leave_requests")
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leaveRequestId;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "start_date", nullable = false)
    private String startDate;

    @Column(name = "end_date", nullable = false)
    private String endDate;

    @Column(name = "request_date", nullable = false)
    private LocalDateTime requestDate;

    @Column(name= "type_of_leave", nullable = false)
    private String typeOfLeave;

    @Column(name = "status", nullable = true)
    @Enumerated(EnumType.STRING)
    private LeaveRequestStatus status;
}
