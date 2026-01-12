package com.saham.hr_system.modules.analytics.dto;

import lombok.Data;

@Data
public class TotalAbsenceDto {
    private long totalAbsences;
    public TotalAbsenceDto(long total){
        this.totalAbsences = total;
    }
}
