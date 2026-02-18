package com.saham.hr_system.modules.analytics.controller;

import com.saham.hr_system.modules.analytics.service.implementation.EmployeeAnalyticsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1/analytics/employees")
public class EmployeeAnalyticsController {

    private final EmployeeAnalyticsServiceImpl employeeAnalyticsService;

    public EmployeeAnalyticsController(EmployeeAnalyticsServiceImpl employeeAnalyticsService) {
        this.employeeAnalyticsService = employeeAnalyticsService;
    }

    @GetMapping("/overview")
    public ResponseEntity<Object> getEmployeeOverview(
            @RequestParam(defaultValue = "ALL") String department,
            @RequestParam(defaultValue = "ALL") String entity
    ){
        try{
            return ResponseEntity.ok(employeeAnalyticsService.getEmployeeAnalyticsOverview(department, entity));
        }catch (RuntimeException e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
