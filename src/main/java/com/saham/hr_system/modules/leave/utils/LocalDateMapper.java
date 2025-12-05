package com.saham.hr_system.modules.leave.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class LocalDateMapper {

    public String mapToFrenchFormat(LocalDate date) {
        if(date == null) {
            return null;
        }
        // otherwise, convert it to a string
        String toStr = date.toString(); // "yyyy-MM-dd"
        String[] parts = toStr.split("-");

        return String.format("%s/%s/%s", parts[2], parts[1], parts[0]);
    }
}
