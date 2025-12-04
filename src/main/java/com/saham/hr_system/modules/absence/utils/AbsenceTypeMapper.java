package com.saham.hr_system.modules.absence.utils;

import org.springframework.stereotype.Component;

@Component
public class AbsenceTypeMapper {

    public String mapToReadableFormat(String absenceType) {
        return switch (absenceType) {
            case "SICKNESS" -> "Absence Maladie";
            case "REMOTE_WORK" -> "Télétravail";
            default -> "Unknown Absence Type";
        };
    }
}
