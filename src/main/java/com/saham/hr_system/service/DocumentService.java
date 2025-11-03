package com.saham.hr_system.service;

import com.saham.hr_system.dto.DocumentRequestDto;
import com.saham.hr_system.dto.DocumentRequestResponseDto;

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
}
