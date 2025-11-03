package com.saham.hr_system.unit;

import com.saham.hr_system.repository.DocumentRequestRepository;
import com.saham.hr_system.service.implementation.DocumentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class DocumentServiceUnitTest {

    @Mock
    private DocumentRequestRepository documentRequestRepository;

    @InjectMocks
    private DocumentServiceImpl documentService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }
}
