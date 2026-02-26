package com.saham.hr_system.utils;

import com.saham.hr_system.modules.holidays.model.Holiday;
import com.saham.hr_system.modules.holidays.repository.HolidayRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class is responsible for calculating the number of leave days between two dates.
 * Excluding the holidays and weekends.
 */
@Component
@Slf4j
public class TotalDaysCalculator {

    private final HolidayRepository holidayRepository;
    private static final String[] weekEnds = {"SATURDAY", "SUNDAY"};


    @Autowired
    public TotalDaysCalculator(HolidayRepository holidayRepository) {
        this.holidayRepository = holidayRepository;
    }
    public long calculateTotalDays(
            LocalDate from,
            LocalDate to
    ){

        // Fetch the holidays from the database in range:
        List<Holiday> holidays = holidayRepository
                .findAllByStartDateBetween(from, to);
        log.info("Fetched holidays from database: {}", holidays);
        // Expand Holiday days into a Set
        Set<LocalDate> holidayDates =
                holidays
                        .stream().flatMap(h -> h.getStartDate().datesUntil(h.getEndDate()))
                        .collect(Collectors.toSet());

        log.info("Expanded holiday dates: {}", holidayDates);
        return
                from.datesUntil(to)
                        .filter(date-> !isWeekend(date))
                        .filter(date -> !holidayDates.contains(date))
                        .count(); // Exclude the end date from the count
    }

    /**
     * Filter a given list of dates from weekends
     * @param dateList
     * @return a list filtered from weekends
     */
    private List<LocalDate> filterDatesFromWeekEnds(List<LocalDate> dateList){
        return dateList
                .stream()
                .filter(date -> !List.of(weekEnds).contains(date.getDayOfWeek().toString()))
                .toList();

    }

    /**
     * Filter a given range of dates from national Holidays stored in the database.
     * @param dateList
     * @return a list of dates that do not contain holidays to calculate the total leave days and balance to be deducted from employee balance
     */
    public List<LocalDate> filterDatesFromHolidays(List<LocalDate> dateList){
        List<Holiday> holidays = holidayRepository.findAll();
        // filter the given date list from holidays dates
        return dateList.stream()
                // filter function, iterate through each date in the list
                .filter(date -> holidays.stream().noneMatch(holiday -> {
                    // Get the dates that do not match the holiday dates
                    List<LocalDate> holidayDates = holiday.getStartDate().datesUntil(
                            holiday.getStartDate().plusDays(holiday.getLeaveDays())
                    ).toList(); // Convert the holiday date into list of covered dates
                    return holidayDates.contains(date); // return a list of dates that does not contain holiday dates
                }))
                .toList();
    }

    private boolean isWeekend(LocalDate date){
        return List.of(weekEnds).contains(date.getDayOfWeek().toString());
    }
}
