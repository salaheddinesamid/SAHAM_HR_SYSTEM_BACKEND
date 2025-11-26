package com.saham.hr_system.modules.absence.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class AbsenceRequestDto {

    private String type;
    private LocalDate startDate;
    private LocalDate endDate;

    private MultipartFile medicalCertificate; // this is optional only for remote work absence
}
