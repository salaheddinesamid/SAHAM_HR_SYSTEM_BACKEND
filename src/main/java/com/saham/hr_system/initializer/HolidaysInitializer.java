package com.saham.hr_system.initializer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saham.hr_system.modules.holidays.model.Holiday;
import com.saham.hr_system.modules.holidays.model.HolidayStatus;
import com.saham.hr_system.modules.holidays.model.HolidayType;
import com.saham.hr_system.modules.holidays.repository.HolidayRepository;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class HolidaysInitializer implements CommandLineRunner {
    private final ObjectMapper objectMapper;
    private final HolidayRepository holidayRepository;


    //private final static
    @Override
    public void run(String... args) throws Exception {
        List<HolidayObject> holidays = objectMapper
                .readValue(new File("src/main/resources/static/moroccan_holidays.json"), new TypeReference<List<HolidayObject>>() {});
        if(holidayRepository.count() == 0) {
            holidays
                    .forEach(h -> {
                        Holiday holiday = new Holiday();
                        holiday.setName(h.getName()); // set the name
                        holiday.setStartDate(h.getStartDate()); // set the start date
                        holiday.setEndDate(h.getEndDate()); // set the end date
                        holiday.setFloating(h.isFloating()); // set if it's floating
                        holiday.setType(HolidayType.valueOf(h.getType())); // set the type
                        holiday.setLastUpdate(LocalDateTime.now()); // set the last update
                        holiday.setLeaveDays(h.getLeaveDays()); // set the total leave days                        holiday.setStatus(HolidayStatus.valueOf(h.getStatus()));

                        // save:
                        holidayRepository.save(holiday);
                    });
        }else{
            log.info("Holidays already exist");
        }

    }
}

@Data
class HolidayObject{
    private LocalDate startDate;
    private LocalDate endDate;
    private String name;
    private String type;
    private boolean isFloating;
    private int leaveDays;
    private String status;
}
