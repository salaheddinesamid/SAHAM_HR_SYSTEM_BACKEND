package com.saham.hr_system.modules.absence.service;

import com.saham.hr_system.modules.absence.dto.AbsenceRequestDto;
import com.saham.hr_system.modules.absence.model.AbsenceRequest;

/**
 * Strategy interface for processing different types of absence requests.
 * <p>
 * Implementations of this interface handle the validation, business rules,
 * and creation of an {@link AbsenceRequest} based on a specific absence type.
 * This design allows the system to support multiple absence types
 * (e.g., sickness, remote work, vacation) with dedicated processors.
 * </p>
 */
public interface AbsenceRequestProcessor {

    /**
     * Determines whether this processor supports handling the given absence type.
     *
     * @param type the absence type submitted by the employee
     * @return {@code true} if this processor can handle the given type, otherwise {@code false}
     */
    boolean supports(String type);

    /**
     * Processes and creates a new {@link AbsenceRequest} based on the submitted request DTO.
     * <p>
     * Implementations should perform validation, apply business rules,
     * and handle any required document uploads (e.g., medical certificates).
     * </p>
     *
     * @param email      the email of the employee submitting the request
     * @param requestDto the input data for the absence request
     * @return the created {@link AbsenceRequest} entity
     * @throws Exception if validation fails, document handling fails, or business rules prevent submission
     */
    AbsenceRequest processAbsenceRequest(String email, AbsenceRequestDto requestDto) throws Exception;
}
