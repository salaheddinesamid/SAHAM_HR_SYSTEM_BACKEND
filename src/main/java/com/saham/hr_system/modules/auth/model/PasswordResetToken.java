package com.saham.hr_system.modules.auth.model;

import com.saham.hr_system.modules.employees.model.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "password_reset_tokens", indexes = {
        @Index(name = "idx_token", columnList = "token")
})
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token")
    private String token;

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate; // Store as epoch milliseconds for easier comparison

    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiryDate);
    }
}
