package com.saham.hr_system.modules.holidays.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "holidays")
public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "VARCHAR(255) DEFAULT 'PENDING' ")
    private HolidayStatus status;

    @Enumerated(EnumType.STRING)
    private HolidayType type;

    private boolean floating;

    @Column(name = "leave_days")
    private int leaveDays;

    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    @PrePersist
    void setStatus(){
        if(this.isFloating()){
            this.status = HolidayStatus.PENDING;
        }else{
            this.status = HolidayStatus.CONFIRMED;
        }
    }
}
