package com.saham.hr_system.modules.payroll.utils;

import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class PayrollPdfFileNameGenerator {
    public String generateUniqueFileName(
            String matriculationNumber,
            int month,
            int year
    ){
        String uniqueId =
                Base64.getUrlEncoder()
                        .withoutPadding()
                        .encodeToString(String.valueOf(matriculationNumber).getBytes())
                        .substring(0,6);
        return String
                .format("BP_%s_%02d_%d.pdf",uniqueId,
                        month,
                        year);
    }
}
