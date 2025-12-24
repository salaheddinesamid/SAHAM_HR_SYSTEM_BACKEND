package com.saham.hr_system.modules.holidays.service.implementation;

import com.saham.hr_system.modules.holidays.dto.NewHolidayDto;
import com.saham.hr_system.modules.holidays.model.Holiday;
import com.saham.hr_system.modules.holidays.repository.HolidayRepository;
import com.saham.hr_system.modules.holidays.service.HolidayAdderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class HolidayAdderServiceImpl implements HolidayAdderService {

    private final HolidayRepository holidayRepository;

    @Autowired
    public HolidayAdderServiceImpl(HolidayRepository holidayRepository) {
        this.holidayRepository = holidayRepository;
    }

    @Override
    public Holiday addHoliday(NewHolidayDto dto) {
        // check if the holiday exists
        if(holidayRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Holiday with the same name already exists");
        } else {
            Holiday holiday = new Holiday();
            holiday.setName(dto.getName());
            holiday.setDate(dto.getDate());
            holiday.setLeaveDays(dto.getLeaveDays());

            holiday.setLastUpdate(LocalDateTime.now());
            return holidayRepository.save(holiday);
        }
    }
}
