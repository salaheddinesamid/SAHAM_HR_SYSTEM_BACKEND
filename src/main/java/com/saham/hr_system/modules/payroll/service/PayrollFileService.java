package com.saham.hr_system.modules.payroll.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface PayrollFileService {
    /**
     * Read payroll PDF document.
     * @param payrollFile: The uploaded payroll PDF file.
     */
    PDDocument loadPayrollPDF(MultipartFile payrollFile) throws IOException;

    /**
     * Split payroll PDF document into individual employee payslips.
     * @param document: The one PDF file to be split.
     * @return list of individual pages.
     */
    List<PDDocument> splitPayrollPDF(PDDocument document) throws IOException;

    /**
     * Process payroll PDF document and map employee identifiers to their payslips.
     * @param document: the payroll PDF document.
     * @throws IOException
     * @return: a map of employee identifier to their respective PDDocument payslip
     */
    Map<String, PDDocument> processPayrollPDF(List<PDDocument> document) throws IOException;

    /**
     * Save individual employee payroll PDF document.
     * @param matriculation: Employee Matriculation
     * @param month: The month of payroll
     * @param year: The year of payroll
     * @param fileName: The fullName of the file to be saved
     * @param document: The PDF Document to be saved
     * @throws IOException
     */
    void savePayrollPDF(int matriculation, int month, int year, String fileName, PDDocument document) throws IOException;
}
