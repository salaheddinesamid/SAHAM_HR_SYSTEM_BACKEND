package com.saham.hr_system.modules.payroll.service.implementation;

import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.modules.payroll.model.PayrollHistory;
import com.saham.hr_system.modules.payroll.model.PayrollHistoryStatus;
import com.saham.hr_system.modules.payroll.repository.PayrollHistoryRepository;
import com.saham.hr_system.modules.payroll.service.PayrollProcessor;
import com.saham.hr_system.modules.payroll.utils.PayrollPdfFileNameGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
@Service
public class PayrollProcessorImpl implements PayrollProcessor {
    private final PayrollFileServiceImpl payrollFileService;
    private final PayrollHistoryRepository payrollHistoryRepository;
    private final PayrollPdfFileNameGenerator payrollPdfFileNameGenerator;

    private final static Logger log = LoggerFactory.getLogger(PayrollProcessorImpl.class);

    @Autowired
    public PayrollProcessorImpl(PayrollFileServiceImpl payrollFileService, PayrollHistoryRepository payrollHistoryRepository, PayrollPdfFileNameGenerator payrollPdfFileNameGenerator) {
        this.payrollFileService = payrollFileService;
        this.payrollHistoryRepository = payrollHistoryRepository;
        this.payrollPdfFileNameGenerator = payrollPdfFileNameGenerator;
    }

    @Override
    @Transactional
    public void processPayroll(
            int month,
            int year,
            MultipartFile file
    ) throws IOException {
        PayrollHistory payrollHistory = new PayrollHistory();
        payrollHistory.setExecutionDate(LocalDateTime.now());
        try{
            int succeedPayrollCount = 0;
            int failedPayrollCount = 0;
            payrollHistory.setStatus(PayrollHistoryStatus.EXECUTED);
            // Load the Payroll PDF:
            PDDocument document = payrollFileService.loadPayrollPDF(file);
            // Split the PDF into individual employee payrolls:
            List<PDDocument> employeeDocs = payrollFileService.splitPayrollPDF(document);
            // Extract matriculation and map documents
            Map<String, PDDocument> employeesPayrollMap = payrollFileService
                    .processPayrollPDF(employeeDocs);
            // Iterate over each employee payroll map, save the payroll PDF file, and update counts
            for (String matriculation : employeesPayrollMap.keySet()){
                log.info("Processing payroll for employee matriculation: " + matriculation);
                try{
                    String fileName = payrollPdfFileNameGenerator.generateUniqueFileName(
                            matriculation,
                            month,
                            year
                    );
                    PDDocument employeePayrollDoc = employeesPayrollMap.get(matriculation);
                    payrollFileService
                            .savePayrollPDF(
                                    matriculation,
                                    month,
                                    year,
                                    fileName,
                                    employeePayrollDoc
                            );
                    succeedPayrollCount += 1;
                    log.info("Successfully processed payroll for employee matriculation: " + matriculation);
                }catch (IOException exception){
                    throw new IOException();
                }
            }
            // set the payroll history details:
            payrollHistory.setSucceededPayrolls(succeedPayrollCount);
            payrollHistory.setFailedPayrolls(failedPayrollCount);
            // set the total number of payrolls
            payrollHistory.setTotalPayrolls(employeesPayrollMap.size());
            // set the month and year of the payroll:
            payrollHistory.setPayrollMonth(month);
            payrollHistory.setPayrollYear(year);
            payrollHistory.setStatus(PayrollHistoryStatus.SUCCEED);
            // save the payroll in the db:
            payrollHistoryRepository.save(payrollHistory);
        }catch (RuntimeException exception){
            payrollHistory.setStatus(PayrollHistoryStatus.FAILED);
            payrollHistoryRepository.save(payrollHistory);
            throw new RuntimeException();
        }
    }
}
