package com.saham.hr_system.modules.documents.model;
import com.saham.hr_system.modules.employees.model.Employee;
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

    @Column(name = "reference_number", unique = true, nullable = false, columnDefinition = "VARCHAR(255) default ''")
    private String referenceNumber;

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
