package com.saham.hr_system.unit;

import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.documents.repository.DocumentRequestRepository;
import com.saham.hr_system.modules.documents.service.implementation.DocumentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class DocumentServiceUnitTest {

    @Mock
    private DocumentRequestRepository documentRequestRepository;

    @InjectMocks
    private DocumentServiceImpl documentService;

    private Employee employee;
    private Employee HR;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRequestDocumentSuccess(){}
}
