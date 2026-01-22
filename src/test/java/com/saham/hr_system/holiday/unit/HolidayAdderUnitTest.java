package com.saham.hr_system.holiday.unit;

import com.saham.hr_system.modules.holidays.repository.HolidayRepository;
import com.saham.hr_system.modules.holidays.service.implementation.HolidayAdderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class HolidayAdderUnitTest {
    @Mock
    private HolidayRepository holidayRepository;

    @InjectMocks
    private HolidayAdderServiceImpl holidayAdderService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddNewHolidaySuccess(){}

    @Test
    void testAddNewHolidayFailureDueToDuplicate(){}
}
