package com.saham.hr_system.holiday.unit;

import com.saham.hr_system.modules.holidays.repository.HolidayRepository;
import com.saham.hr_system.modules.holidays.service.implementation.ReligiousHolidayModifier;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ReligiousHolidayModifierUnitTest {

    @Mock
    private HolidayRepository holidayRepository;

    @InjectMocks
    private ReligiousHolidayModifier religiousHolidayModifier;
    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }
}
