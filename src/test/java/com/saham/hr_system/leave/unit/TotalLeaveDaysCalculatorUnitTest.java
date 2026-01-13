package com.saham.hr_system.leave.unit;

import com.saham.hr_system.modules.holidays.model.Holiday;
import com.saham.hr_system.modules.holidays.repository.HolidayRepository;
import com.saham.hr_system.utils.TotalDaysCalculator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@Slf4j
public class TotalLeaveDaysCalculatorUnitTest {

    @Mock
    private HolidayRepository holidayRepository;

    @InjectMocks
    private TotalDaysCalculator totalLeaveDaysCalculator;

    Holiday holiday;
    Holiday holiday2;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);

        holiday = new Holiday();
        holiday.setName("New Year's Day");
        holiday.setStartDate(LocalDate.of(2026, 1, 1));
        holiday.setLeaveDays(2);

        holiday2 = new Holiday();
        holiday2.setName("Independence Day");
        holiday2.setStartDate(LocalDate.of(2026, 1, 3));
        holiday2.setLeaveDays(2);
    }

    @Test
    void testFilterDateListFromHolidays(){
        // Arrange
        when(holidayRepository.findAll()).thenReturn(List.of(holiday, holiday2));

        // Act:
        List<LocalDate> filteredDates = totalLeaveDaysCalculator.filterDatesFromHolidays(
                List.of(
                        LocalDate.of(2026, 1, 1),
                        LocalDate.of(2026, 1, 2),
                        LocalDate.of(2026, 1, 3),
                        LocalDate.of(2026, 1, 4),
                        LocalDate.of(2026, 1, 5)
                )
        );

        log.info("Filtered Dates: {}", filteredDates);
    }

    @Test
    void testCalculateTotalLeaveDays(){
        // Arrange:
        when(holidayRepository.findAll()).thenReturn(List.of(holiday, holiday2));

        // Act and verify:
        assertEquals(11, totalLeaveDaysCalculator.calculateTotalDays(
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 15)
        ));
    }
}
