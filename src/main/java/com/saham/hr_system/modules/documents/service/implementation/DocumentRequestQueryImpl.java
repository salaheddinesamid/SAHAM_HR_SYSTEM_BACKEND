package com.saham.hr_system.modules.documents.service.implementation;

import com.saham.hr_system.modules.documents.dto.DocumentRequestResponseDto;
import com.saham.hr_system.modules.documents.repository.DocumentRequestRepository;
import com.saham.hr_system.modules.documents.service.DocumentRequestQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentRequestQueryImpl implements DocumentRequestQuery {
    private final DocumentRequestRepository documentRequestRepository;

    @Autowired
    public DocumentRequestQueryImpl(DocumentRequestRepository documentRequestRepository) {
        this.documentRequestRepository = documentRequestRepository;
    }

    @Override
    public List<DocumentRequestResponseDto> getAllDocumentRequests(String email, int page, int size) {
        return List.of();
    }

    @Override
    public List<DocumentRequestResponseDto> getAllEmployeesRequests(int page, int size) {
        return List.of();
    }
}
