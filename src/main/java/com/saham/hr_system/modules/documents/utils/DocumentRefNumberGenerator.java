package com.saham.hr_system.modules.documents.utils;

import com.saham.hr_system.modules.documents.model.DocumentRequest;
import com.saham.hr_system.modules.documents.repository.DocumentRequestRepository;
import com.saham.hr_system.modules.employees.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
@Component
public class DocumentRefNumberGenerator {
    private final static String PREFIX = "DOC";

    private final DocumentRequestRepository documentRequestRepository;
    @Autowired
    public DocumentRefNumberGenerator(DocumentRequestRepository documentRequestRepository) {
        this.documentRequestRepository = documentRequestRepository;
    }

    public String generate(DocumentRequest documentRequest) {
        Employee employee = documentRequest.getEmployee();
        String fingerprint =
                Base64
                        .getUrlEncoder()
                        .withoutPadding()
                        .encodeToString(employee.getEmail().getBytes())
                        .substring(0, 6);
        String todayPart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long employeeRequestCount = documentRequestRepository.countByEmployee(employee);

        return
                String.format("%s%s%s%04d",PREFIX,fingerprint,todayPart,employeeRequestCount);
    }
}
