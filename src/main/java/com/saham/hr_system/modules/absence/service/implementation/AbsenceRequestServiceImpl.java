package com.saham.hr_system.modules.absence.service.implementation;

import com.saham.hr_system.modules.absence.dto.AbsenceRequestDetails;
import com.saham.hr_system.modules.absence.dto.AbsenceRequestDto;
import com.saham.hr_system.modules.absence.dto.AbsenceResponseDto;
import com.saham.hr_system.modules.absence.model.Absence;
import com.saham.hr_system.modules.absence.model.AbsenceRequest;
import com.saham.hr_system.modules.absence.service.AbsenceRequestProcessor;
import com.saham.hr_system.modules.absence.service.AbsenceRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AbsenceRequestServiceImpl implements AbsenceRequestService {

    private final List<AbsenceRequestProcessor> processors;

    @Autowired
    public AbsenceRequestServiceImpl(List<AbsenceRequestProcessor> processors) {
        this.processors = processors;
    }

    @Override
    public AbsenceRequestDetails requestAbsence(String email, AbsenceRequestDto requestDto) throws Exception {
        AbsenceRequestProcessor processor =
                processors.stream().filter(p -> p.supports(requestDto.getType()))
                        .findFirst().orElse(null);
        // process the absence request:
        assert processor != null;
        AbsenceRequest absence = processor.processAbsenceRequest(email, requestDto);
        // return the response dto:
        return new AbsenceRequestDetails(absence);
    }
}
