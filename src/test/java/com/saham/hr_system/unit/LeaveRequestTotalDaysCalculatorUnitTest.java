package com.saham.hr_system.unit;

import com.saham.hr_system.modules.leave.utils.LeaveDaysCalculator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LeaveRequestTotalDaysCalculatorUnitTest {


    private final LeaveDaysCalculator leaveDaysCalculator = new LeaveDaysCalculator();

    @Test
    void testCalculateTotalDays(){
        LocalDate startDate = LocalDate.of(2025, 11, 27);
        LocalDate endDate = LocalDate.of(2025, 12, 1);

        double totalDays = leaveDaysCalculator.calculateTotalDays(startDate, endDate);
        // verify:
        assertEquals(3.0, totalDays);
    }
}
