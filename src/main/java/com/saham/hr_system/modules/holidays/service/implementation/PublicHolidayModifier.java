package com.saham.hr_system.modules.holidays.service.implementation;

import com.saham.hr_system.modules.holidays.dto.HolidayModificationDto;
import com.saham.hr_system.modules.holidays.dto.HolidayUpdatedEvent;
import com.saham.hr_system.modules.holidays.model.Holiday;
import com.saham.hr_system.modules.holidays.model.HolidayType;
import com.saham.hr_system.modules.holidays.repository.HolidayRepository;
import com.saham.hr_system.modules.holidays.service.HolidayModifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class PublicHolidayModifier implements HolidayModifier {
    private final HolidayRepository holidayRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public PublicHolidayModifier(HolidayRepository holidayRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.holidayRepository = holidayRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public boolean supports(String type) {
        return HolidayType.PUBLIC.equals(HolidayType.valueOf(type));
    }

    @Override
    public Holiday modifyHoliday(String name, HolidayModificationDto dto) {
        // fetch the holiday from the db:
        Holiday holiday =
                holidayRepository.findByName(name)
                        .orElseThrow();
        if(dto.getStartDate() != null || dto.getEndDate() != null){
            updateHolidayDates(holiday, dto);
        }
        if(dto.getName() != null){
            holiday.setName(dto.getName());
        }
        if(dto.getLeaveDays() != 0){
            holiday.setLeaveDays(dto.getLeaveDays());
        }
        // save and return the updated holiday:
        return holidayRepository.save(holiday);
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
    }
}
