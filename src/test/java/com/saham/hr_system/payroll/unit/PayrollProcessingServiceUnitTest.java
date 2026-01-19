package com.saham.hr_system.payroll.unit;

import com.saham.hr_system.modules.payroll.repository.PayrollHistoryRepository;
import com.saham.hr_system.modules.payroll.service.implementation.PayrollFileServiceImpl;
import com.saham.hr_system.modules.payroll.service.implementation.PayrollProcessorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class PayrollProcessingServiceUnitTest {
    @Mock
    private PayrollFileServiceImpl payrollFileService;

    @Mock
    private PayrollHistoryRepository payrollHistoryRepository;
    @InjectMocks
    private PayrollProcessorImpl payrollProcessor;
    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessPayrollSuccess() throws IOException {
        File file = new File("src/test/java/com/saham/hr_system/payroll/unit/sample_payroll.pdf");
        InputStream is = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile(
                    "sample_payroll.pdf",
                    "sample_payroll.pdf",
                    "application/pdf",
                    is
        );
        payrollProcessor.processPayroll(
                02, 2026, multipartFile
        );
    }
}
