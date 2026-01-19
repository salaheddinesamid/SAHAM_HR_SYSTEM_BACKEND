package com.saham.hr_system.payroll.unit;

import com.saham.hr_system.modules.payroll.service.implementation.PayrollFileServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
public class PayrollFileServiceUnitTest {
    @Mock
    private Splitter splitter;

    private final PayrollFileServiceImpl payrollFileService = new PayrollFileServiceImpl("");
    private File file;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        file = new File("src/test/java/com/saham/hr_system/payroll/unit/sample_payroll.pdf");
    }

    @Test
    void testLoadPayrollDocumentSuccess(){}

    @Test
    void shouldSplitPdfIntoSinglePageDocuments() throws IOException {
        InputStream is = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile(
                "sample_payroll.pdf",
                "sample_payroll.pdf",
                "application/pdf",
                is
        );
        PDDocument document = PDDocument.load(multipartFile.getInputStream());
        List<PDDocument> results = payrollFileService.splitPayrollPDF(document);

        assertNotNull(results);
        assertEquals(3, results.size());
    }

    @Test
    void testPdfToTextConversionSuccess() throws IOException {
        File file = new File("src/test/java/com/saham/hr_system/payroll/unit/sample_payroll.pdf");
        InputStream is = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile(
                "sample_payroll.pdf",
                "sample_payroll.pdf",
                "application/pdf",
                is
        );

        PDDocument document = PDDocument.load(multipartFile.getInputStream());
        PDFTextStripper textStripper = new PDFTextStripper();
        String text = textStripper.getText(document);
        System.out.println("PDF Text content: " + text);
    }
    @Test
    void testProcessPayrollPDFSuccess() throws IOException {
        InputStream is = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile(
                "sample_payroll.pdf",
                "sample_payroll.pdf",
                "application/pdf",
                is
        );
        PDDocument document = PDDocument.load(multipartFile.getInputStream());
        List<PDDocument> employeesDocs = payrollFileService.splitPayrollPDF(document);

        // Act:
        Map<String, PDDocument> results = payrollFileService.processPayrollPDF(employeesDocs);
        // Verify:
        assertEquals(3, results.keySet().size());
    }
}
