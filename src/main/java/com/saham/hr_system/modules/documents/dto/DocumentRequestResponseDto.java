package com.saham.hr_system.modules.documents.dto;

import com.saham.hr_system.modules.documents.model.DocumentRequest;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DocumentRequestResponseDto {

    private Long id;
    private String requestedBy;
    private String refNumber;
    private String documents;
    private LocalDateTime requestDate;
    private String status;

    public DocumentRequestResponseDto(
            DocumentRequest request
    ) {
       this.id = request.getRequestId();
       this.requestedBy = request.getEmployee().getFullName();
       this.refNumber = request.getReferenceNumber();
       this.documents = request.getDocuments();
       this.requestDate = request.getRequestDate();
       this.status = request.getStatus().toString();
    }
}
