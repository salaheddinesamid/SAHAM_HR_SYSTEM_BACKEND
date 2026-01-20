package com.saham.hr_system.modules.payroll.service.implementation;

import com.saham.hr_system.modules.payroll.service.PayrollFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Component
@Slf4j

public class PayrollFileServiceImpl implements PayrollFileService {
    private final Path uploadPath;
    private static List<String> allowedExtension = List.of("pdf");
    private static final String password = "T315730"; // for testing purposes

    public PayrollFileServiceImpl(@Value("${file.upload.payrolls}") String path) {
        this.uploadPath = Paths.get(path).toAbsolutePath().normalize();
    }

    @Override
    public PDDocument loadPayrollPDF(MultipartFile payrollFile) throws IOException {
        log.info("Loading payroll PDF file.");
        return PDDocument.load(payrollFile.getInputStream(), password);
    }

    /**
     * Splits a payroll PDF document into individual employee payroll documents.
     * @param document: the PDF document
     * @return a list of PDDocument, each representing an individual employee's payroll
     */
    @Override
    public List<PDDocument> splitPayrollPDF(PDDocument document) throws IOException {
        log.info("Starting to split payroll PDF document.");

        Splitter splitter = new Splitter();
        splitter.setSplitAtPage(1);
        log.info("Total pages in document: {}", document.getNumberOfPages());

        List<PDDocument> result = splitter.split(document);
        document.close(); // close AFTER splitting
        return result;
    }


    @Override
    public Map<String, PDDocument> processPayrollPDF(List<PDDocument> documents) throws IOException {
        Map<String, PDDocument> employeesPayrollMap =
                new HashMap<>();
        PDFTextStripper stripper = new PDFTextStripper();
        documents
                .forEach(document -> {
                    try {
                        String text = stripper.getText(document);
                        String matriculation = extractEmployeeMatriculationNumber(text);
                        log.info("Extracted matriculation number: {}", matriculation);
                        employeesPayrollMap.put(matriculation, document);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        return employeesPayrollMap;
    }

    private String extractEmployeeMatriculationNumber(String fullText) {
        String[] splitText = fullText.split("\n");
        String matriculation = "";
        for(int i = 0; i < splitText.length; i++) {
            if(splitText[i].contains("Matriculation")) {
                String[] parts = splitText[i].split(" ");
                matriculation = parts[2];
                matriculation = matriculation.replace("\r", "").trim();
            }
        }
        log.info("Extracted matriculation number: {}", matriculation);
        return matriculation;
    }
    @Override
    public void savePayrollPDF(String matriculationNumber,
                               int month,
                               int year,
                               String fileName, PDDocument document) throws IOException {
        // Check if the file is empty:
        if(document == null) {
            throw new IllegalArgumentException("The payroll document is null.");
        }
        Path target = uploadPath.resolve(matriculationNumber)
                        .resolve(String.valueOf(year))
                                .resolve(String.valueOf(month));
        // Check if the directory already exists:
        if(!Files.isDirectory(target)){
            Files.createDirectories(target);
        }

        Path copyPath = target
                .resolve(fileName);

        document.save(copyPath.toFile());
        document.close();
        log.info("Saved payroll file {}", target);
    }
}
