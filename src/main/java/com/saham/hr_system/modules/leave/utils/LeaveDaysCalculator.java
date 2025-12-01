package com.saham.hr_system.modules.leave.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * This class is responsible for calculating the number of leave days between two dates.
 * Excluding the holidays and weekends.
 */
@Component
@Slf4j
public class LeaveDaysCalculator {

    private static final String[] weekEnds = {"SATURDAY", "SUNDAY"};

    public long calculateTotalDays(
            LocalDate from,
            LocalDate to
    ){
        // Get the days list:
        List<LocalDate> dateList =
            from.datesUntil(to.plusDays(1)).toList();
        // calculate the total days, excluding the weekends
        long totalDays = 0;
        totalDays =
                dateList.stream().filter(
                        date -> !List.of(weekEnds).contains(date.getDayOfWeek().toString())
                ).count();
        log.info("Total leave days calculated: {}", totalDays);
        return totalDays;

    }
}
