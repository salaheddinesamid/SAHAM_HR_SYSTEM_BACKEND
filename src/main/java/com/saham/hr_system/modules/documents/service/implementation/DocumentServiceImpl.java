package com.saham.hr_system.modules.documents.service.implementation;

import com.saham.hr_system.modules.documents.dto.DocumentRequestDto;
import com.saham.hr_system.modules.documents.dto.DocumentRequestResponseDto;
import com.saham.hr_system.modules.documents.model.DocumentRequest;
import com.saham.hr_system.modules.documents.model.DocumentRequestStatus;
import com.saham.hr_system.modules.documents.service.DocumentService;
import com.saham.hr_system.modules.documents.utils.DocumentRefNumberGenerator;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.documents.repository.DocumentRequestRepository;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final EmployeeRepository employeeRepository;;
    private final DocumentRequestRepository documentRequestRepository;
    private final DocumentRequestEmailSenderImpl documentRequestEmailSender;
    private final DocumentRefNumberGenerator documentRefNumberGenerator;

    @Autowired
    public DocumentServiceImpl(EmployeeRepository employeeRepository, DocumentRequestRepository documentRequestRepository, DocumentRequestEmailSenderImpl documentRequestEmailSender, DocumentRefNumberGenerator documentRefNumberGenerator) {
        this.employeeRepository = employeeRepository;
        this.documentRequestRepository = documentRequestRepository;
        this.documentRequestEmailSender = documentRequestEmailSender;
        this.documentRefNumberGenerator = documentRefNumberGenerator;
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
        String refNumber = documentRefNumberGenerator
                .generate(documentRequest); // generate the unique reference number
        documentRequest.setReferenceNumber(refNumber); // set the reference number

        // save the request:
        documentRequestRepository.save(documentRequest);

        // notify the employee and HR:
        CompletableFuture.runAsync(()->{
            try{
                documentRequestEmailSender.notifyEmployee(documentRequest);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
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

    @Override
    public List<DocumentRequestResponseDto> getAllEmployeesRequests() {
        // Fetch document requests that are in process for approval:
        List<DocumentRequest> documentRequests =
                documentRequestRepository.findAllByStatus(DocumentRequestStatus.IN_PROCESS);
        // return mapped response:
        return
                documentRequests.stream()
                        .map(DocumentRequestResponseDto::new)
                        .toList();
    }
}
