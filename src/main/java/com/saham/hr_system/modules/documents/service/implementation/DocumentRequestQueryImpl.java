package com.saham.hr_system.modules.documents.service.implementation;

import com.saham.hr_system.exception.UserNotFoundException;
import com.saham.hr_system.modules.documents.dto.DocumentRequestResponseDto;
import com.saham.hr_system.modules.documents.model.DocumentRequest;
import com.saham.hr_system.modules.documents.model.DocumentRequestStatus;
import com.saham.hr_system.modules.documents.repository.DocumentRequestRepository;
import com.saham.hr_system.modules.documents.service.DocumentRequestQuery;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DocumentRequestQueryImpl implements DocumentRequestQuery {
    private final DocumentRequestRepository documentRequestRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public DocumentRequestQueryImpl(DocumentRequestRepository documentRequestRepository, EmployeeRepository employeeRepository) {
        this.documentRequestRepository = documentRequestRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Page<DocumentRequestResponseDto> getAllDocumentRequests(String email, int page, int size) {
        try{
            //fetch the employee
            Employee employee = employeeRepository.findByEmail(email)
                    .orElseThrow(() -> new UserNotFoundException(email));


            Pageable pageable = PageRequest.of(page, size);
            Page<DocumentRequest> requests =
                    documentRequestRepository.findAllByEmployee(employee, pageable);

            return requests
                    .map(DocumentRequestResponseDto::new);
        }catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<DocumentRequestResponseDto> getAllEmployeesRequests(int page, int size) {
        Page<DocumentRequest> requests =
                documentRequestRepository.findAllByStatus(DocumentRequestStatus.IN_PROCESS,PageRequest.of(page, size));

        return requests
                .map(DocumentRequestResponseDto::new);
    }
}
