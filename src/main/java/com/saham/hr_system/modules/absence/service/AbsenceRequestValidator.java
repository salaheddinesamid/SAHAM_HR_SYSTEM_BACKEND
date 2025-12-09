package com.saham.hr_system.modules.absence.service;

import com.saham.hr_system.modules.absence.dto.AbsenceRequestDto;

/**
 * Interface for validating absence requests.
 * <p>
 * Implementations of this interface should enforce rules for absence requests,
 * such as date consistency and required document presence.
 */
public interface AbsenceRequestValidator {

    /**
     * Validates the given {@link AbsenceRequestDto}.
     * <p>
     * Implementations may throw exceptions if the request data is invalid,
     * for example, if the start date is after the end date or a required
     * document is missing.
     *
     * @param requestDto the absence request to validate
     * @throws Exception if validation fails
     */
    void validate(AbsenceRequestDto requestDto) throws Exception;
}
