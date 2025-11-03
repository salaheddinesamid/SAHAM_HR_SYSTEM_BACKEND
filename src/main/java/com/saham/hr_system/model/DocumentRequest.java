package com.saham.hr_system.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "document_requests")
public class DocumentRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    @Column(name = "documents")
    private String documents;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private DocumentRequestStatus status;

    @Column(name = "request_date")
    private LocalDateTime requestDate;

}
