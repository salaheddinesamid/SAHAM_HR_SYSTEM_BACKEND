package com.saham.hr_system.modules.payroll.service.implementation;

import com.saham.hr_system.modules.payroll.dto.PayrollDetailsDto;
import com.saham.hr_system.modules.payroll.service.PayrollFileService;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

@Component
public class PayrollFileServiceImpl implements PayrollFileService {
    private final Path uploadPath;
    private static List<String> allowedExtension = List.of("pdf");
    private static final String password = "T315730"; // for testing purposes
    private final PayrollTokenServiceImpl payrollTokenService;

    private final static Logger log = LoggerFactory.getLogger(PayrollFileServiceImpl.class);

    public PayrollFileServiceImpl(@Value("${file.upload.payrolls}") String path, PayrollTokenServiceImpl payrollTokenService) {
        this.uploadPath = Paths.get(path).toAbsolutePath().normalize();
        this.payrollTokenService = payrollTokenService;
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
        try{
            log.info("Starting to split payroll PDF document.");

            Splitter splitter = new Splitter();
            splitter.setSplitAtPage(1);
            log.info("Total pages in document: {}", document.getNumberOfPages());

            List<PDDocument> result = splitter.split(document);
            document.close(); // close AFTER splitting
            return result;
        }catch (RuntimeException exception){
            log.error("Error while splitting payroll PDF document: {}", exception.getMessage());
            throw new IOException("Failed to split the payroll PDF document.", exception);
        }
    }


    @Override
    public Map<String, PDDocument> processPayrollPDF(List<PDDocument> documents) throws IOException {
        try{
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
        }catch (RuntimeException e){
            log.error("Error while processing payroll PDF documents: {}", e.getMessage());
            throw new IOException("Failed to process the payroll PDF documents.", e);
        }
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

    @Override
    public List<PayrollDetailsDto> getPayrollsOverview(String matriculation, int year) throws IOException {
        Path path = uploadPath
                .resolve(matriculation)
                .resolve(String.valueOf(year));
        if(!Files.exists(path)){
            return List.of();
        }

        try(Stream<Path> months = Files.list(path)){
            return
                    months
                            .filter(Files::isDirectory)
                            .map(m-> {
                                int month = Integer.parseInt(m.getFileName().toString());

                                String downloadToken = payrollTokenService.generateToken(
                                        matriculation,
                                        month,
                                        year
                                );
                                return new PayrollDetailsDto(
                                        month,
                                        "/api/v1/payrolls/download/" + downloadToken
                                );
                            })
                            .toList();
        }
    }

    @Override
    public Resource downloadPayroll(String token) {
        // Verify the token:
        // Extract the claims (matriculation, month, year):
        // return the file as Resource:
        return null;
    }
}
