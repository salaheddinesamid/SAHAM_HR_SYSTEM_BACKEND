package com.saham.hr_system.modules.absence.service;

import com.saham.hr_system.modules.absence.dto.AbsenceRequestDto;

public interface AbsenceRequestValidator {

    void validate(AbsenceRequestDto requestDto) throws Exception;
}
