package com.saham.hr_system.modules.payroll.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PayrollProcessor {
    /**
     *
     * @param file
     */
    void processPayroll(int month, int year, MultipartFile file) throws IOException;
}
