package com.saham.hr_system.modules.payroll.service.implementation;

import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.modules.payroll.model.PayrollHistory;
import com.saham.hr_system.modules.payroll.model.PayrollHistoryStatus;
import com.saham.hr_system.modules.payroll.repository.PayrollHistoryRepository;
import com.saham.hr_system.modules.payroll.service.PayrollProcessor;
import com.saham.hr_system.modules.payroll.utils.PayrollPdfFileNameGenerator;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;

import java.util.List;
import java.util.Map;
@Service
public class PayrollProcessorImpl implements PayrollProcessor {
    private final PayrollFileServiceImpl payrollFileService;
    private final EmployeeRepository employeeRepository;
    private final PayrollHistoryRepository payrollHistoryRepository;
    private final PayrollPdfFileNameGenerator payrollPdfFileNameGenerator;

    @Autowired
    public PayrollProcessorImpl(PayrollFileServiceImpl payrollFileService, EmployeeRepository employeeRepository, PayrollHistoryRepository payrollHistoryRepository, PayrollPdfFileNameGenerator payrollPdfFileNameGenerator) {
        this.payrollFileService = payrollFileService;
        this.employeeRepository = employeeRepository;
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
        try{
            payrollHistory.setStatus(PayrollHistoryStatus.EXECUTED);
            // Load the Payroll PDF:
            PDDocument document = payrollFileService.loadPayrollPDF(file);
            // Split the PDF into individual employee payrolls:
            List<PDDocument> employeeDocs = payrollFileService.splitPayrollPDF(document);
            // Extract matriculation and map documents
            Map<String, PDDocument> employeesPayrollMap = payrollFileService
                    .processPayrollPDF(employeeDocs);
            System.out.println(employeesPayrollMap.keySet());
            /*
            for(String key: employeesPayrollMap.keySet()){
                // Check if the employee exists by the Matriculation number
                if(employeeRepository.existsByMatriculation(key)){
                    // Save the payroll file for the employee
                    payrollFileService.savePayrollPDF(
                            payrollPdfPathGenerator.generatePath(
                                    key,
                                    month,
                                    year
                            )

                    );
                }
            }

             */
            payrollHistory.setStatus(PayrollHistoryStatus.SUCCEED);
            // Save the payroll history
            payrollHistoryRepository.save(payrollHistory);
        }catch (RuntimeException exception){
            payrollHistory.setStatus(PayrollHistoryStatus.FAILED);
            payrollHistoryRepository.save(payrollHistory);
            throw new RuntimeException();
        }
    }
}
