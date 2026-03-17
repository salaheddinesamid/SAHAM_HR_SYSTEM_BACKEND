package com.saham.hr_system.modules.payroll.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
/** * Service interface for processing payroll data from uploaded files.
 */
public interface PayrollProcessor {
    /**
     * Processes the payroll data for a given month and year from the provided file.
     *
     * @param month the month for which to process payroll (1-12)
     * @param year the year for which to process payroll
     * @param file the uploaded file containing payroll data
     * @throws IOException if there is an error reading the file
     */
    void processPayroll(int month, int year, MultipartFile file) throws IOException;
}
