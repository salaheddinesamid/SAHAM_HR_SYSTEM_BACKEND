package com.saham.hr_system.modules.holidays.controller;

import com.saham.hr_system.modules.holidays.dto.HolidayModificationDto;
import com.saham.hr_system.modules.holidays.dto.NewHolidayDto;
import com.saham.hr_system.modules.holidays.model.Holiday;
import com.saham.hr_system.modules.holidays.service.implementation.HolidayAdderServiceImpl;
import com.saham.hr_system.modules.holidays.service.implementation.HolidayModifierImpl;
import com.saham.hr_system.modules.holidays.service.implementation.HolidayQueryServiceImpl;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/holidays")
public class HolidaysController {

    private final HolidayQueryServiceImpl holidayQueryService;
    private final HolidayAdderServiceImpl holidayAdderService;
    private final HolidayModifierImpl holidayModifier;

    @Autowired
    public HolidaysController(HolidayQueryServiceImpl holidayQueryService, HolidayAdderServiceImpl holidayAdderService, HolidayModifierImpl holidayModifier) {
        this.holidayQueryService = holidayQueryService;
        this.holidayAdderService = holidayAdderService;
        this.holidayModifier = holidayModifier;
    }

    /**
     * @return
     */
    @GetMapping("get_all")
    public ResponseEntity<List<Holiday>> getAllHolidays() {
        List<Holiday> holidays = holidayQueryService.getAllHolidays();
        return ResponseEntity.ok(holidays);
    }


    @PostMapping("new")
    public ResponseEntity<?> addNewHoliday(@RequestBody NewHolidayDto newHolidayDto) {
        Holiday response = holidayAdderService.addHoliday(newHolidayDto);
        return ResponseEntity
                .status(200)
                .body(response);
    }

    @PutMapping("update/{name}")
    public ResponseEntity<?> updateHoliday(@PathVariable String name, @RequestBody HolidayModificationDto holidayModificationDto) {
        Holiday response =
                holidayModifier.modifyHoliday(name, holidayModificationDto);

        return ResponseEntity.status(200).body(response);
    }
}
