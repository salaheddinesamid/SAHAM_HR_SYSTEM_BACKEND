package com.saham.hr_system.modules.documents.service;

import com.saham.hr_system.modules.documents.dto.DocumentRequestResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DocumentRequestQuery {
    /**
     * Get all document requests made by an employee.
     * @param email
     * @param page
     * @param size
     * @return list of document requests.
     */
    Page<DocumentRequestResponseDto> getAllDocumentRequests(String email, int page , int size);

    /**
     * This function returns all document requests that needs HR approval
     * @param page
     * @param size
     * @return list of document requests
     *
     */
    Page<DocumentRequestResponseDto> getAllEmployeesRequests(int page , int size);
}
