package com.saham.hr_system.modules.payroll.controller;

import com.saham.hr_system.modules.payroll.service.implementation.PayrollProcessorImpl;
import com.saham.hr_system.modules.payroll.service.implementation.PayrollQueryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
/** * This controller handles all the endpoints related to payroll processing and querying.
 * It allows HR to upload payroll data and employees to query their payroll history and overview.
 */
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
    /**     * This endpoint allows HR to upload payroll data for a specific month and year. The uploaded file is processed to extract payroll information.
     *
     * @param month the month for which the payroll data is being uploaded
     * @param year the year for which the payroll data is being uploaded
     * @param file the multipart file containing the payroll data
     * @return a ResponseEntity indicating the success of the upload operation
     * @throws IOException if an error occurs while processing the uploaded file
     */
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
    /**     * This endpoint allows employees to get an overview of their payrolls for a specific year. The employee is identified by their matriculation number.
     *
     * @param year the year for which the payroll overview is requested
     * @param matriculation the matriculation number of the employee
     * @return a ResponseEntity containing the payroll overview for the specified year and employee
     * @throws IOException if an error occurs while fetching the payroll overview
     */
    @GetMapping("overview")
    public ResponseEntity<?> getOverview(
            @RequestParam int year,
            @RequestParam String matriculation
    ) throws IOException {
        return ResponseEntity.ok(
                payrollQueryService.getYearlyPayrolls(
                        matriculation,
                        year
                )
        );
    }
    /**     * This endpoint allows employees to get the history of all their payrolls. The employee is identified by their matriculation number.
     *
     * @return a ResponseEntity containing the history of all payrolls for the authenticated employee
     */
    @GetMapping("history/get_all")
    public ResponseEntity<?> getAllPayrollsHistory(){
        return ResponseEntity.ok(payrollQueryService.getAllPayrollsHistory());
    }
}
