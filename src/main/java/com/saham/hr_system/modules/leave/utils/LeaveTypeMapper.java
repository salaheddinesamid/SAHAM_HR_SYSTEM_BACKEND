package com.saham.hr_system.modules.leave.utils;

import org.springframework.stereotype.Component;

@Component
public class LeaveTypeMapper {

    public String mapLeaveType(String type) {
        return switch (type) {
            case "EXCEPTIONAL" -> "Exceptionnel";
            case "ANNUAL" -> "Annuel";
            default -> type; // fallback
        };
    }

}
