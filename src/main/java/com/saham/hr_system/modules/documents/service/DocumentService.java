package com.saham.hr_system.modules.documents.service;

import com.saham.hr_system.modules.documents.dto.DocumentRequestDto;
import com.saham.hr_system.modules.documents.dto.DocumentRequestResponseDto;

import java.util.List;

public interface DocumentService {

    /**
     * Request a document for an employee.
     * @param email
     * @param requestDto
     */
    void requestDocument(String email, DocumentRequestDto requestDto);

    /**
     * Get all document requests made by an employee.
     * @param email
     * @return list of document requests.
     */
    List<DocumentRequestResponseDto> getAllDocumentRequests(String email);

    /**
     * This function returns all document requests that needs HR approval
     * @return list of document requests
     */
    List<DocumentRequestResponseDto> getAllEmployeesRequests();
}
