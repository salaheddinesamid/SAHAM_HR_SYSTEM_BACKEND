package com.saham.hr_system.holiday.unit;

import com.saham.hr_system.modules.holidays.repository.HolidayRepository;
import com.saham.hr_system.modules.holidays.service.implementation.PublicHolidayModifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class PublicHolidayModifierUnitTest {
    @Mock
    private HolidayRepository holidayRepository;

    @InjectMocks
    private PublicHolidayModifier publicHolidayModifier;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testModifyPublicHolidaySuccess(){}

    @Test
    void testModifyPublicHolidayThrowHolidayNotFound(){}
}
