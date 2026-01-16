package com.saham.hr_system.batches;

import com.saham.hr_system.modules.holidays.repository.HolidayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HolidayAnnualUpdateBatch {
    private final HolidayRepository holidayRepository;
}
