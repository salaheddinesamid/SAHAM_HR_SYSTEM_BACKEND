package com.saham.hr_system.modules.holidays.service.implementation;

import com.saham.hr_system.modules.holidays.dto.HolidayModificationDto;
import com.saham.hr_system.modules.holidays.exception.HolidayNotFoundException;
import com.saham.hr_system.modules.holidays.model.Holiday;
import com.saham.hr_system.modules.holidays.repository.HolidayRepository;
import com.saham.hr_system.modules.holidays.service.HolidayModifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class HolidayModifierImpl implements HolidayModifier {
    private final HolidayRepository holidayRepository;

    @Autowired
    public HolidayModifierImpl(HolidayRepository holidayRepository) {
        this.holidayRepository = holidayRepository;
    }

    @Override
    public Holiday modifyHoliday(String name, HolidayModificationDto dto) {
        Holiday holiday =
                holidayRepository.findByName(name)
                        .orElseThrow(()-> new HolidayNotFoundException(name));

        holiday.setName(dto.getName());
        holiday.setDate(dto.getDate());
        holiday.setLeaveDays(dto.getLeaveDays());
        holiday.setLastUpdate(LocalDateTime.now());

        // save the holiday:
        return holidayRepository.save(holiday);


    }
}
