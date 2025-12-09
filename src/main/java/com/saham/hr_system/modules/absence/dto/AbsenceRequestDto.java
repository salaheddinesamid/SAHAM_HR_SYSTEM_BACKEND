package com.saham.hr_system.modules.absence.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

/**
 * DTO representing the input payload for submitting a new absence request.
 * <p>
 * This object is typically submitted from a form and may contain an optional
 * medical certificate for sickness-related absence types.
 * </p>
 */
@Data
public class AbsenceRequestDto {

    /** The type of absence requested (SICKNESS, REMOTE_WORK, VACATION, etc.). */
    private String type;

    /** Requested start date of the absence period. */
    private LocalDate startDate;

    /** Requested end date of the absence period. */
    private LocalDate endDate;

    /**
     * Optional medical certificate file.
     * <p>
     * Only required for certain absence types, such as sickness absence.
     * </p>
     */
    private MultipartFile medicalCertificate;
}
