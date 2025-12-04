package com.saham.hr_system.modules.absence.service;

import com.saham.hr_system.modules.absence.dto.AbsenceRequestDetails;
import com.saham.hr_system.modules.absence.dto.AbsenceRequestDto;
import com.saham.hr_system.modules.absence.dto.AbsenceResponseDto;

/**
 *
 */
public interface AbsenceRequestService {

    AbsenceRequestDetails requestAbsence(String email, AbsenceRequestDto requestDto) throws Exception;
    void approveAbsenceRequest(String approvedBy, String refNumber) throws Exception;
    void approveAbsence(String refNumber) throws Exception;
}
