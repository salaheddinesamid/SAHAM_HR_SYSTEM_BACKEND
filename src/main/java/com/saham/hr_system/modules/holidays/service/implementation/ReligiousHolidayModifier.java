package com.saham.hr_system.modules.holidays.service.implementation;

import com.saham.hr_system.modules.holidays.dto.HolidayModificationDto;
import com.saham.hr_system.modules.holidays.dto.HolidayUpdatedEvent;
import com.saham.hr_system.modules.holidays.exception.HolidayDateCannotBeUpdated;
import com.saham.hr_system.modules.holidays.model.Holiday;
import com.saham.hr_system.modules.holidays.model.HolidayType;
import com.saham.hr_system.modules.holidays.repository.HolidayRepository;
import com.saham.hr_system.modules.holidays.service.HolidayModifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReligiousHolidayModifier implements HolidayModifier {
    private final HolidayRepository holidayRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public ReligiousHolidayModifier(HolidayRepository holidayRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.holidayRepository = holidayRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public boolean supports(String type) {
        return HolidayType.RELIGIOUS.equals(HolidayType.valueOf(type));
    }

    @Override
    public Holiday modifyHoliday(String name, HolidayModificationDto dto) {
        // fetch the holiday from the db:
        Holiday holiday =
                holidayRepository.findByName(name)
                        .orElseThrow();
        // check if the holiday is floating:
        if(!holiday.isFloating()){
            throw new HolidayDateCannotBeUpdated(name);
        }
        // otherwise:
        if(dto.getStartDate() != null && dto.getEndDate() != null){
            holiday.setStartDate(dto.getStartDate());
            holiday.setEndDate(dto.getEndDate());
            long totalDaysUpdate =
                    dto.getEndDate().toEpochDay() - dto.getStartDate().toEpochDay() + 1;
            // publish the event of holiday update
            log.info("Publishing holiday update event for holiday: {}, total days updated: {}", name, totalDaysUpdate);
            applicationEventPublisher
                    .publishEvent(
                            new HolidayUpdatedEvent(
                                    holiday,
                                    totalDaysUpdate
                            )
                    );
        }
        if(dto.getName() != null){
            holiday.setName(dto.getName());
        }
        if(dto.getLeaveDays() != 0){
            holiday.setLeaveDays(dto.getLeaveDays());
        }

        return holidayRepository.save(holiday);
    }
}
