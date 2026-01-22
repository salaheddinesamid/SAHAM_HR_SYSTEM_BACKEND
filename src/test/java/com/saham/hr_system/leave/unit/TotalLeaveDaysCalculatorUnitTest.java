package com.saham.hr_system.leave.unit;

import com.saham.hr_system.modules.holidays.model.Holiday;
import com.saham.hr_system.modules.holidays.repository.HolidayRepository;
import com.saham.hr_system.utils.TotalDaysCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TotalLeaveDaysCalculatorUnitTest {

    @Mock
    private HolidayRepository holidayRepository;

    @InjectMocks
    private TotalDaysCalculator totalLeaveDaysCalculator;

    private Holiday holiday1;
    private Holiday holiday2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        holiday1 = new Holiday();
        holiday1.setName("New Year's Day");
        holiday1.setStartDate(LocalDate.of(2026, 1, 1));
        holiday1.setEndDate(LocalDate.of(2026, 1, 2));
        holiday1.setLeaveDays(1); // Jan 1 & Jan 2

        holiday2 = new Holiday();
        holiday2.setName("Independence Day");
        holiday2.setStartDate(LocalDate.of(2026, 1, 3));
        holiday2.setEndDate(LocalDate.of(2026, 1, 4));
        holiday2.setLeaveDays(1); // Jan 3 & Jan 4
    }

    @Test
    void shouldFilterHolidayDatesFromDateList() {
        // Arrange
        when(holidayRepository.findAll()).thenReturn(List.of(holiday1, holiday2));

        List<LocalDate> inputDates = List.of(
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 2),
                LocalDate.of(2026, 1, 3),
                LocalDate.of(2026, 1, 4),
                LocalDate.of(2026, 1, 5)
        );

        // Act
        List<LocalDate> result =
                totalLeaveDaysCalculator.filterDatesFromHolidays(inputDates);

        // Assert
        assertEquals(3, result.size());
        assertTrue(result.contains(LocalDate.of(2026, 1, 5)));

        verify(holidayRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnSameListWhenNoHolidaysExist() {
        // Arrange
        when(holidayRepository.findAll()).thenReturn(List.of());

        List<LocalDate> inputDates = List.of(
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 2)
        );

        // Act
        List<LocalDate> result =
                totalLeaveDaysCalculator.filterDatesFromHolidays(inputDates);

        // Assert
        assertEquals(2, result.size());
        assertEquals(inputDates, result);
    }

    @Test
    void shouldCalculateTotalLeaveDaysExcludingHolidays() {
        LocalDate start = LocalDate.of(2026, 1, 1);
        LocalDate end = LocalDate.of(2026, 1, 9);
        // Arrange
        when(holidayRepository.findAllByStartDateBetween(
                start, end
        )).thenReturn(List.of(holiday1, holiday2));

        // Act
        long totalDays =
                totalLeaveDaysCalculator.calculateTotalDays(start, end);

        // Total days: 9
        // Holidays: 2 days (Jan 1–4)
        // Week ends: 2 days (Jan 3, 4)
        // Expected: 5 days
        assertEquals(6, totalDays);

        verify(holidayRepository, times(1)).findAllByStartDateBetween(
                start, end
        );
    }

    @Test
    void shouldReturnZeroWhenStartDateIsAfterEndDate() {
        // Act
        assertThrows(IllegalArgumentException.class, ()-> {
            totalLeaveDaysCalculator.calculateTotalDays(
                    LocalDate.of(2026, 1, 10),
                    LocalDate.of(2026, 1, 1)
            );
        });
    }
}
