package com.saham.hr_system.modules.analytics.controller;

import com.saham.hr_system.modules.analytics.service.implementation.AbsenceAnalyticsServiceImpl;
import com.saham.hr_system.modules.analytics.service.implementation.LeaveAnalyticsServiceImpl;
import com.saham.hr_system.modules.analytics.service.implementation.LoanAnalyticsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("api/v1/analytics")

public class AnalyticsController {
    private final AbsenceAnalyticsServiceImpl absenceAnalyticsService;
    private final LeaveAnalyticsServiceImpl leaveAnalyticsService;
    private final LoanAnalyticsServiceImpl loanAnalyticsService;

    public AnalyticsController(AbsenceAnalyticsServiceImpl absenceAnalyticsService, LeaveAnalyticsServiceImpl leaveAnalyticsService, LoanAnalyticsServiceImpl loanAnalyticsService) {
        this.absenceAnalyticsService = absenceAnalyticsService;
        this.leaveAnalyticsService = leaveAnalyticsService;
        this.loanAnalyticsService = loanAnalyticsService;
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

    @GetMapping("/absences/overview")
    public ResponseEntity<Object> getAbsencesOverview(
            @RequestParam(defaultValue = "ALL") String type,
            @RequestParam(defaultValue = "ALL") String department,
            @RequestParam(defaultValue = "ALL") String entity,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to
    ){

        Object response = absenceAnalyticsService.getAbsenceAnalyticsOverview(
                type, from, to, department, entity
        );
        return ResponseEntity
                .status(200)
                .body(response);
    }

    @GetMapping("/loans/overview")
    public ResponseEntity<Object> getLoansOverview(
            @RequestParam(defaultValue = "ALL") String type,
            @RequestParam(defaultValue = "ALL") String department,
            @RequestParam(defaultValue = "ALL") String entity,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to
    ){

        Object response = loanAnalyticsService.getLoanAnalyticsOverview(
                type, from, to, department, entity
        );
        return ResponseEntity
                .status(200)
                .body(response);
    }
}
