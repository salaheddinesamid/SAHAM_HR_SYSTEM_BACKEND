package com.saham.hr_system.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TotalDaysCalculator {
    public double calculateTotalDays(LocalDate date1, LocalDate date2){
        return date1.until(date1).getDays() + 1;
    }
}
