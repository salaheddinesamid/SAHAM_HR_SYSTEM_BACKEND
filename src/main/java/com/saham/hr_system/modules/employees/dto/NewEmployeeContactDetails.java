package com.saham.hr_system.modules.employees.dto;

import lombok.Data;

@Data
public class NewEmployeeContactDetails {

    private String personToContactInCaseOfEmergency;
    private String emergencyContactNumber;
}
