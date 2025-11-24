package com.saham.hr_system.modules.absence.service.implementation;

import com.saham.hr_system.modules.absence.dto.AbsenceRequestDto;
import com.saham.hr_system.modules.absence.service.AbsenceRequestValidator;
import org.springframework.stereotype.Component;

@Component
public class AbsenceRequestValidatorImpl implements AbsenceRequestValidator {
    @Override
    public void validate(AbsenceRequestDto requestDto) throws Exception {
        if(requestDto.getStartDate().isAfter(requestDto.getEndDate())){
            throw new Exception("Start date cannot be after end date.");
        }
    }
}
