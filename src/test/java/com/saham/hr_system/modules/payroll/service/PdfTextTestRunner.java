package com.saham.hr_system.modules.payroll.service;
import java.io.InputStream;

import com.saham.hr_system.modules.payroll.service.implementation.PayrollProcessorImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class PdfTextTestRunner implements CommandLineRunner {

    private static final String PDF_PASSWORD = "T315730";
    private final PayrollProcessorImpl payrollProcessor;

    public PdfTextTestRunner(PayrollProcessorImpl payrollProcessor) {
        this.payrollProcessor = payrollProcessor;
    }

    @Override
    public void run(String... args) throws Exception {



    }
}
