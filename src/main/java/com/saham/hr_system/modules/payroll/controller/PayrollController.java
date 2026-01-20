package com.saham.hr_system.modules.payroll.controller;

import com.saham.hr_system.modules.payroll.service.implementation.PayrollProcessorImpl;
import com.saham.hr_system.modules.payroll.service.implementation.PayrollQueryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/payrolls")
public class PayrollController {
    private final PayrollProcessorImpl payrollProcessor;
    private final PayrollQueryServiceImpl payrollQueryService;

    @Autowired
    public PayrollController(PayrollProcessorImpl payrollProcessor, PayrollQueryServiceImpl payrollQueryService) {
        this.payrollProcessor = payrollProcessor;
        this.payrollQueryService = payrollQueryService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadPayrollData(
            @RequestParam int month,
            @RequestParam int year,
            @RequestBody MultipartFile file
            ) throws IOException {
        payrollProcessor.processPayroll(
                month,
                year,
                file
        );
        return ResponseEntity.status(200).build();
    }

    @GetMapping("history/get_all")
    public ResponseEntity<?> getAllPayrollsHistory(){
        return ResponseEntity.ok(payrollQueryService.getAllPayrollsHistory());
    }
}
