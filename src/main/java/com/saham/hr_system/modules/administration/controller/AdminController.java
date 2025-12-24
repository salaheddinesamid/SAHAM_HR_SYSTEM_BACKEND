package com.saham.hr_system.modules.administration.controller;

import com.azure.core.annotation.Post;
import com.saham.hr_system.exception.UserNotFoundException;
import com.saham.hr_system.modules.administration.dto.EmployeeDetailsResponseDto;
import com.saham.hr_system.modules.administration.dto.NewEmployeeRequestDto;
import com.saham.hr_system.modules.administration.service.implementation.EmployeeManagementServiceImpl;
import com.saham.hr_system.modules.administration.service.implementation.EmployeeQueryServiceImpl;
import com.saham.hr_system.modules.employees.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final EmployeeQueryServiceImpl employeeQueryService;
    private final EmployeeManagementServiceImpl employeeManagementService;
    @Autowired
    public AdminController(EmployeeQueryServiceImpl employeeQueryService, EmployeeManagementServiceImpl employeeManagementService) {
        this.employeeQueryService = employeeQueryService;
        this.employeeManagementService = employeeManagementService;
    }

    /*
      Employee Management endpoints
     */

    @GetMapping("employees/get_all")
    public ResponseEntity<?> getAllEmployees(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize
    ) {

        Page<EmployeeDetailsResponseDto> employees =
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

    @PostMapping("/employees/new")
    public ResponseEntity<?> addNewEmployee(
            @RequestBody NewEmployeeRequestDto requestDto
    ){
        employeeManagementService.newEmployee(requestDto);
        return ResponseEntity
                .status(200)
                .body("New employee added successfully");
    }
    /*
      Holidays management endpoints
     */


}
