package com.saham.hr_system.modules.employees.controller;

import com.saham.hr_system.modules.employees.dto.EmployeeDetailsDto;
import com.saham.hr_system.modules.employees.dto.SubordinateDetailsResponseDto;
import com.saham.hr_system.modules.employees.service.implementation.EmployeeServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeServiceImpl employeeService;

    public EmployeeController(EmployeeServiceImpl employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("get")
    public ResponseEntity<?> getEmployee(@RequestParam String email){
        EmployeeDetailsDto employee = employeeService.getEmployeeDetails(email);

        return ResponseEntity
                .status(200)
                .body(employee);
    }

    @GetMapping("/subordinates")
    public ResponseEntity<?> getSubordinates(@RequestParam String managerEmail){
        List<SubordinateDetailsResponseDto> response =
                employeeService.getSubordinates(managerEmail);

        return ResponseEntity
                .status(200)
                .body(response);
    }
}
