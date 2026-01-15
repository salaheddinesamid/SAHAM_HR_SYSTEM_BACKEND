package com.saham.hr_system.modules.holidays.service.implementation;

import com.saham.hr_system.modules.holidays.dto.HolidayModificationDto;
import com.saham.hr_system.modules.holidays.dto.HolidayUpdatedEvent;
import com.saham.hr_system.modules.holidays.exception.HolidayNotFoundException;
import com.saham.hr_system.modules.holidays.model.Holiday;
import com.saham.hr_system.modules.holidays.repository.HolidayRepository;
import com.saham.hr_system.modules.holidays.service.HolidayModifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class HolidayModifierImpl implements HolidayModifier {
    private final HolidayRepository holidayRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public HolidayModifierImpl(HolidayRepository holidayRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.holidayRepository = holidayRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public Holiday modifyHoliday(String name, HolidayModificationDto dto) {
        Holiday holiday =
                holidayRepository.findByName(name)
                        .orElseThrow(()-> new HolidayNotFoundException(name));

        holiday.setName(dto.getName());
        holiday.setStartDate(dto.getStartDate());
        holiday.setEndDate(dto.getEndDate());
        holiday.setLeaveDays(dto.getLeaveDays());
        holiday.setLastUpdate(LocalDateTime.now());
        Holiday updatedHoliday = holidayRepository.save(holiday);

        // publish the event of holiday update:
        applicationEventPublisher
                .publishEvent(
                        new HolidayUpdatedEvent(updatedHoliday)
                );

        // save the holiday:
        return updatedHoliday;
    }
}
