package com.saham.hr_system.modules.absence.service.implementation;

import com.saham.hr_system.modules.absence.dto.AbsenceRequestDto;
import com.saham.hr_system.modules.absence.model.AbsenceRequest;
import com.saham.hr_system.modules.absence.model.AbsenceRequestStatus;
import com.saham.hr_system.modules.absence.model.AbsenceType;
import com.saham.hr_system.modules.absence.service.AbsenceRequestMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Implementation of {@link AbsenceRequestMapper} that maps
 * {@link AbsenceRequestDto} to {@link AbsenceRequest} entities.
 * <p>
 * This implementation sets default values such as the current issue date
 * and the initial request status.
 */
@Component
public class AbsenceRequestMapperImpl implements AbsenceRequestMapper {

    /**
     * Maps an {@link AbsenceRequestDto} to a new {@link AbsenceRequest} entity.
     * <p>
     * Sets the following default values:
     * <ul>
     *     <li>issueDate: current timestamp</li>
     *     <li>status: {@link AbsenceRequestStatus#IN_PROCESS}</li>
     *     <li>type: parsed from the DTO type string</li>
     * </ul>
     *
     * @param requestDto the DTO containing absence request data
     * @return the mapped {@link AbsenceRequest} entity
     */
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
