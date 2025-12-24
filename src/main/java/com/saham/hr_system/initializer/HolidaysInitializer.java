package com.saham.hr_system.initializer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saham.hr_system.modules.holidays.model.Holiday;
import com.saham.hr_system.modules.holidays.repository.HolidayRepository;
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
                        holiday.setName(h.getName());
                        holiday.setDate(h.getDate());
                        holiday.setLastUpdate(LocalDateTime.now());
                        holiday.setLeaveDays(h.getLeaveDays());
                        // save:
                        holidayRepository.save(holiday);
                    });
        }else{
            log.info("Holidays already exist");
        }

    }
}

class HolidayObject{
    private LocalDate date;
    private String name;
    private String type;
    private int leaveDays;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLeaveDays() {
        return leaveDays;
    }

    public void setLeaveDays(int leaveDays) {
        this.leaveDays = leaveDays;
    }
}
