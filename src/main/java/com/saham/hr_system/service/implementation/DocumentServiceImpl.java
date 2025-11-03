package com.saham.hr_system.service.implementation;

import com.saham.hr_system.dto.DocumentRequestDto;
import com.saham.hr_system.dto.DocumentRequestResponseDto;
import com.saham.hr_system.model.DocumentRequest;
import com.saham.hr_system.model.DocumentRequestStatus;
import com.saham.hr_system.model.Employee;
import com.saham.hr_system.repository.DocumentRequestRepository;
import com.saham.hr_system.repository.EmployeeRepository;
import com.saham.hr_system.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final EmployeeRepository employeeRepository;;
    private final DocumentRequestRepository documentRequestRepository;

    @Autowired
    public DocumentServiceImpl(EmployeeRepository employeeRepository, DocumentRequestRepository documentRequestRepository) {
        this.employeeRepository = employeeRepository;
        this.documentRequestRepository = documentRequestRepository;
    }

    @Override
    public void requestDocument(String email, DocumentRequestDto requestDto) {
        // Fetch the employee from db:
        Employee employee = employeeRepository.findByEmail(email).orElse(null);

        // Create and save the document request:
        DocumentRequest documentRequest = new DocumentRequest();
        String documents = processDocumentName(requestDto.getDocuments());
        documentRequest.setDocuments(documents);
        documentRequest.setEmployee(employee);
        documentRequest.setRequestDate(LocalDateTime.now());
        documentRequest.setStatus(DocumentRequestStatus.IN_PROCESS);

        // save the request:
        documentRequestRepository.save(documentRequest);
    }

    private String processDocumentName(List<String> documentName) {
        StringBuilder names = new StringBuilder();
        for (String name : documentName) {
            names.append(name).append(", ");
        }
        // Remove the trailing comma and space
        if (!names.isEmpty()) {
            names.setLength(names.length() - 2);
        }
        return names.toString();
    }

    @Override
    public List<DocumentRequestResponseDto> getAllDocumentRequests(String email) {
        // Fetch the employee from db:
        Employee employee = employeeRepository.findByEmail(email).orElse(null);

        List<DocumentRequest> requests = documentRequestRepository.findAllByEmployee(employee);

        return requests.stream()
                .map(DocumentRequestResponseDto::new)
                .toList();
    }
}
