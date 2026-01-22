package com.saham.hr_system.modules.holidays.controller;

import com.saham.hr_system.modules.holidays.dto.HolidayModificationDto;
import com.saham.hr_system.modules.holidays.dto.HolidayResponseDto;
import com.saham.hr_system.modules.holidays.dto.NewHolidayDto;
import com.saham.hr_system.modules.holidays.model.Holiday;
import com.saham.hr_system.modules.holidays.service.HolidayModifier;
import com.saham.hr_system.modules.holidays.service.implementation.HolidayAdderServiceImpl;
import com.saham.hr_system.modules.holidays.service.implementation.HolidayQueryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/holidays")
public class HolidaysController {

    private final HolidayQueryServiceImpl holidayQueryService;
    private final HolidayAdderServiceImpl holidayAdderService;
    private final List<HolidayModifier> holidayModifiers;

    @Autowired
    public HolidaysController(HolidayQueryServiceImpl holidayQueryService, HolidayAdderServiceImpl holidayAdderService, List<HolidayModifier> holidayModifiers) {
        this.holidayQueryService = holidayQueryService;
        this.holidayAdderService = holidayAdderService;
        this.holidayModifiers = holidayModifiers;
    }

    /**
     * @return
     */
    @GetMapping("get_all")
    public ResponseEntity<?> getAllHolidays() {
        HolidayResponseDto responseDto = holidayQueryService.getAllHolidays();
        return ResponseEntity.ok(responseDto);
    }


    @PostMapping("new")
    public ResponseEntity<?> addNewHoliday(@RequestBody NewHolidayDto newHolidayDto) {
        Holiday response = holidayAdderService.addHoliday(newHolidayDto);
        return ResponseEntity
                .status(200)
                .body(response);
    }

    @PatchMapping("update/{name}/{type}")
    public ResponseEntity<?> updateHoliday(@PathVariable String name, @PathVariable String type, @RequestBody HolidayModificationDto holidayModificationDto) {
        HolidayModifier modifier =
            holidayModifiers.stream()
                    .filter(m-> m.supports(type))
                    .findFirst().orElseThrow();
        Holiday response =
                modifier.modifyHoliday(name, holidayModificationDto);

        return ResponseEntity.status(200).body(response);
    }
}
