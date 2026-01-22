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
    public boolean supports(String type) {
        return false;
    }

    @Override
    public Holiday modifyHoliday(String name, HolidayModificationDto dto) {
        Holiday holiday =
                holidayRepository.findByName(name)
                        .orElseThrow(()-> new HolidayNotFoundException(name));
        if(dto.getName()!= null){
            holiday.setName(dto.getName());
        }
        // Update the start and end dates of the holiday
        if(dto.getStartDate() != null || dto.getEndDate() != null){
            updateHolidayDates(holiday, dto);
        }
        if(dto.getLeaveDays() != 0){
            holiday.setLeaveDays(dto.getLeaveDays());

        }
        holiday.setLastUpdate(LocalDateTime.now());
        Holiday updatedHoliday = holidayRepository.save(holiday);


        // save the holiday:
        return updatedHoliday;
    }
    private void updateHolidayDates(Holiday holiday, HolidayModificationDto dto) {
        // update the start date if not null
        if (dto.getStartDate() != null) {
            holiday.setStartDate(dto.getStartDate());
        }
        // update the end date if not null
        if (dto.getEndDate() != null) {
            holiday.setEndDate(dto.getEndDate());
        }
        // publish the event of holiday update:
        applicationEventPublisher
                .publishEvent(
                        new HolidayUpdatedEvent(holiday, 0)
                );
        // save the holdiay update
    }
}
