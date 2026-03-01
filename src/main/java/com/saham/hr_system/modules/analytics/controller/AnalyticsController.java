package com.saham.hr_system.modules.analytics.controller;

import com.azure.core.annotation.Get;
import com.saham.hr_system.modules.analytics.dto.AvgAbsenceDurationDto;
import com.saham.hr_system.modules.analytics.dto.TotalAbsenceDto;
import com.saham.hr_system.modules.analytics.dto.TotalAbsenceRequestDto;
import com.saham.hr_system.modules.analytics.service.AbsenceAnalyticsService;
import com.saham.hr_system.modules.analytics.service.implementation.LeaveAnalyticsServiceImpl;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1/analytics")

public class AnalyticsController {
    private final List<AbsenceAnalyticsService> absenceAnalyticsServices;
    private final LeaveAnalyticsServiceImpl leaveAnalyticsService;

    public AnalyticsController(List<AbsenceAnalyticsService> absenceAnalyticsServices, LeaveAnalyticsServiceImpl leaveAnalyticsService) {
        this.absenceAnalyticsServices = absenceAnalyticsServices;
        this.leaveAnalyticsService = leaveAnalyticsService;
    }

    @GetMapping("absence/total")
    public ResponseEntity<?> getTotalAbsences(@RequestParam(defaultValue = "ALL") String status, @RequestParam String type){
        AbsenceAnalyticsService analyticsService =
                absenceAnalyticsServices.stream().filter(service-> service.supports(type))
                        .findFirst().orElseThrow();

        TotalAbsenceDto response = analyticsService.getTotalAbsence();
        return ResponseEntity
                .status(200)
                .body(response);
    }

    @GetMapping("/absence/total-requests")
    public ResponseEntity<?> getTotalAbsenceRequests(
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            @RequestParam(required = false, defaultValue = "ALL") String status
            ){
        AbsenceAnalyticsService analyticsService =
                absenceAnalyticsServices.stream().filter(service-> service.supports("REMOTE_WORK"))
                        .findFirst().orElseThrow();

        TotalAbsenceRequestDto response =
                analyticsService.getTotalAbsenceRequests(from, to, status);
        return ResponseEntity
                .status(200)
                .body(response);
    }
    @GetMapping("absence/average")
    public ResponseEntity<?> getAbsenceAVGDuration(@RequestParam String type){
        AbsenceAnalyticsService analyticsService =
                absenceAnalyticsServices.stream().filter(service-> service.supports(type))
                        .findFirst().orElseThrow();

        AvgAbsenceDurationDto response = analyticsService.getAvgAbsenceDuration();
        return ResponseEntity
                .status(200)
                .body(response);
    }

    @GetMapping("/leaves/overview")
    public ResponseEntity<Object> getLeavesOverview(
            @RequestParam(defaultValue = "ALL") String type,
            @RequestParam(defaultValue = "ALL") String department,
            @RequestParam(defaultValue = "ALL") String entity,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to
    ){

        Object response = leaveAnalyticsService.getLeaveAnalyticsOverview(
                type, from, to, department, entity
        );
        return ResponseEntity
                .status(200)
                .body(response);
    }
}
