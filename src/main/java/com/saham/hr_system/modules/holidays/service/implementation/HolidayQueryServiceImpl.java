package com.saham.hr_system.modules.holidays.service.implementation;

import com.saham.hr_system.modules.holidays.dto.HolidayResponseDto;
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
    public HolidayResponseDto getAllHolidays() {
        // Fetch all the holidays from the db:
        List<Holiday> holidayList = holidayRepository.findAll();
        // Calculate the total leave days of the holidays:
        long totalLeaveDays = getTotalHolidayLeaveDays(holidayList);

        return new HolidayResponseDto(
                holidayList,
                totalLeaveDays
        );
    }

    private long getTotalHolidayLeaveDays(List<Holiday> holidays){
        return
                holidays.stream()
                        .map(Holiday::getLeaveDays)
                        .reduce(0, Integer::sum);
    }
}
