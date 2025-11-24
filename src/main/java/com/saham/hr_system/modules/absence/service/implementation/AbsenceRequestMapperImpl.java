package com.saham.hr_system.modules.absence.service.implementation;

import com.saham.hr_system.modules.absence.dto.AbsenceRequestDto;
import com.saham.hr_system.modules.absence.model.AbsenceRequest;
import com.saham.hr_system.modules.absence.model.AbsenceRequestStatus;
import com.saham.hr_system.modules.absence.model.AbsenceType;
import com.saham.hr_system.modules.absence.service.AbsenceRequestMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AbsenceRequestMapperImpl implements AbsenceRequestMapper {
    @Override
    public AbsenceRequest mapToEntity(AbsenceRequestDto requestDto) {
        AbsenceRequest request = new AbsenceRequest();
        request.setStartDate(requestDto.getStartDate());
        request.setEndDate(requestDto.getEndDate());
        request.setIssueDate(LocalDateTime.now());
        request.setType(AbsenceType.valueOf(requestDto.getType()));
        request.setStatus(AbsenceRequestStatus.IN_PROCESS);
        return request;
    }
}
