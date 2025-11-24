package com.saham.hr_system.modules.absence.service;

import com.saham.hr_system.modules.absence.dto.AbsenceRequestDto;
import com.saham.hr_system.modules.absence.model.AbsenceRequest;
import jakarta.validation.constraints.NotNull;

public interface AbsenceRequestMapper {
    AbsenceRequest mapToEntity(@NotNull AbsenceRequestDto requestDto);
}
