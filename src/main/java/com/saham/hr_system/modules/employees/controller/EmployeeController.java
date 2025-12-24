package com.saham.hr_system.modules.employees.controller;

import com.saham.hr_system.exception.UserNotFoundException;
import com.saham.hr_system.modules.employees.dto.EmployeeDetailsDto;
import com.saham.hr_system.modules.employees.dto.NewEmployeeDto;
import com.saham.hr_system.modules.employees.dto.SubordinateDetailsResponseDto;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.service.implementation.EmployeeAdderServiceImpl;
import com.saham.hr_system.modules.employees.service.implementation.EmployeeQueryServiceImpl;
import com.saham.hr_system.modules.employees.service.implementation.EmployeeServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeServiceImpl employeeService;
    private final EmployeeAdderServiceImpl employeeAdderService;
    private final EmployeeQueryServiceImpl employeeQueryService;

    public EmployeeController(EmployeeServiceImpl employeeService, EmployeeAdderServiceImpl employeeAdderService, EmployeeQueryServiceImpl employeeQueryService) {
        this.employeeService = employeeService;
        this.employeeAdderService = employeeAdderService;
        this.employeeQueryService = employeeQueryService;
    }

    @GetMapping("get")
    public ResponseEntity<?> getEmployee(@RequestParam String email){
        EmployeeDetailsDto employee = employeeService.getEmployeeDetails(email);

        return ResponseEntity
                .status(200)
                .body(employee);
    }

    @PostMapping("new")
    public ResponseEntity<?> newEmployee(@RequestBody NewEmployeeDto newEmployee){
        EmployeeDetailsDto response = employeeAdderService
                .add(newEmployee);

        return ResponseEntity
                .status(200)
                .body(response);
    }

    @GetMapping("employees/get_all")
    public ResponseEntity<?> getAllEmployees(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize
    ) {

        Page<EmployeeDetailsDto> employees =
                employeeQueryService.getAllEmployees(pageNumber, pageSize);
        return ResponseEntity
                .status(200)
                .body(employees);
    }
    @GetMapping("managers/verify")
    public ResponseEntity<?> findManager(
            @RequestParam String fullName
    ){
        boolean exists =
                employeeQueryService.verifyManager(fullName);
        return ResponseEntity
                .status(200)
                .body(exists);
    }

    @GetMapping("managers/get")
    public ResponseEntity<?> getManager(
            @RequestParam String fullName
    ){
        try{
            Employee manager =
                    employeeQueryService.getManager(fullName);
            return ResponseEntity
                    .status(200)
                    .body(manager);
        }catch (UserNotFoundException ex){
            throw new UserNotFoundException(ex.getMessage());
        }
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
