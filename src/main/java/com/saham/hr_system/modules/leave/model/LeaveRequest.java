package com.saham.hr_system.modules.leave.model;

import com.saham.hr_system.modules.employees.model.Employee;
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
    private Double totalDays;

    @Column(name = "request_date", nullable = false)
    private LocalDateTime requestDate;

    @Column(name= "type_of_leave", nullable = false)
    @Enumerated(EnumType.STRING)
    private LeaveType typeOfLeave;

    @Column(name = "type_details")
    private String typeDetails;

    @Column(name = "status", nullable = true)
    @Enumerated(EnumType.STRING)
    private LeaveRequestStatus status;

    @Column(name = "approved_by_manager")
    private boolean approvedByManager;

    @Column(name = "approved_by_hr")
    private boolean approvedByHr;

    public double getTotalDays() {
        return totalDays != null ? totalDays : 0.0;
    }

    @PrePersist
    public void prePersist(){
        if(this.approvedByManager && this.approvedByHr){
            this.status = LeaveRequestStatus.APPROVED;
        }
    }
}
