package com.saham.hr_system.modules.absence.service;

import com.saham.hr_system.modules.absence.dto.AbsenceRequestDto;
import com.saham.hr_system.modules.absence.model.AbsenceRequest;
import jakarta.validation.constraints.NotNull;

/**
 * Interface for mapping an {@link AbsenceRequestDto} to an {@link AbsenceRequest} entity.
 * <p>
 * Implementations are responsible for converting data transfer objects into
 * persistence-ready entities for further processing or saving in the database.
 */
public interface AbsenceRequestMapper {

    /**
     * Maps an {@link AbsenceRequestDto} to an {@link AbsenceRequest} entity.
     *
     * @param requestDto the DTO containing absence request data
     * @return the mapped {@link AbsenceRequest} entity
     */
    AbsenceRequest mapToEntity(@NotNull AbsenceRequestDto requestDto);
}
