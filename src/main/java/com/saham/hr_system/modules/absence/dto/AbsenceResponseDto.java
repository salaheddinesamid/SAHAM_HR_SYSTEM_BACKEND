package com.saham.hr_system.modules.absence.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Simplified DTO used to return essential information about an absence request.
 * <p>
 * This DTO is typically used in lists and lightweight API responses.
 * </p>
 */
@Data
public class AbsenceResponseDto {

    /** Unique identifier of the absence request. */
    private Long absenceId;

    /** Full name of the employee who submitted the request. */
    private String requestedBy;

    /** The type/category of the absence. */
    private String absenceType;

    /** Start date of the requested absence period. */
    private LocalDate startDate;

    /** End date of the requested absence period. */
    private LocalDate endDate;

    /** Date and time when the request was created. */
    private LocalDateTime issueDate;
}
