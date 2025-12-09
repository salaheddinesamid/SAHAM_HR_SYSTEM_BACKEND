package com.saham.hr_system.modules.absence.service.implementation;

import com.saham.hr_system.modules.absence.dto.AbsenceRequestDto;
import com.saham.hr_system.modules.absence.service.AbsenceRequestValidator;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link AbsenceRequestValidator} for validating absence requests.
 * <p>
 * This validator checks that the request dates are consistent and ensures required
 * documents are present for specific absence types.
 */
@Component
public class AbsenceRequestValidatorImpl implements AbsenceRequestValidator {

    /**
     * Validates an {@link AbsenceRequestDto}.
     * <p>
     * Checks performed:
     * <ul>
     *     <li>Start date is not after end date</li>
     *     <li>Medical certificate is present for sickness absences</li>
     * </ul>
     *
     * @param requestDto the absence request data transfer object to validate
     * @throws Exception if validation fails
     */
    @Override
    public void validate(AbsenceRequestDto requestDto) throws Exception {
        if (requestDto.getStartDate().isAfter(requestDto.getEndDate())) {
            throw new Exception("Start date cannot be after end date.");
        }

        if (requestDto.getType().equals("SICKNESS")
                && (requestDto.getMedicalCertificate() == null || requestDto.getMedicalCertificate().isEmpty())) {
            throw new Exception("Medical certificate is required for sickness absence.");
        }
    }
}
