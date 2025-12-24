package com.saham.hr_system.modules.holidays.service.implementation;

import com.saham.hr_system.modules.holidays.model.Holiday;
import com.saham.hr_system.modules.holidays.repository.HolidayRepository;
import com.saham.hr_system.modules.holidays.service.HolidayQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HolidayQueryServiceImpl implements HolidayQueryService {
    private final HolidayRepository holidayRepository;

    @Autowired
    public HolidayQueryServiceImpl(HolidayRepository holidayRepository) {
        this.holidayRepository = holidayRepository;
    }

    @Override
    public List<Holiday> getAllHolidays() {
        return holidayRepository
                .findAll();
    }
}
