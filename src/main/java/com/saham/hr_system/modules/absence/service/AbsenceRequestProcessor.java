package com.saham.hr_system.modules.absence.service;

import com.saham.hr_system.modules.absence.dto.AbsenceRequestDto;
import com.saham.hr_system.modules.absence.model.AbsenceRequest;

/**
 *
 */
public interface AbsenceRequestProcessor {

    boolean supports(String type);
    AbsenceRequest processAbsenceRequest(String email, AbsenceRequestDto requestDto) throws Exception;
}
