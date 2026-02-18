package com.saham.hr_system.modules.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeAnalyticsDto {

    private long totalEmployees;
    private long totalActiveEmployees;
    private long totalInactiveEmployees;
    private long totalMaleEmployees;
    private long totalFemaleEmployees;
}
