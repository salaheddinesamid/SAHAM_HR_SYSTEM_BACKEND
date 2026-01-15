package com.saham.hr_system.modules.employees.model;

import com.saham.hr_system.modules.leave.model.Leave;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class LeaveBalanceAdjustment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leaveAdjustmentId;
    @ManyToOne
    private Employee employee;
    @ManyToOne
    private Leave leave;
    private double delta;
    private String reason;
    private LocalDateTime createdAt;
}
