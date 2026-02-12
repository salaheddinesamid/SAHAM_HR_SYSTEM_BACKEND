package com.saham.hr_system.modules.employees.controller;

import com.saham.hr_system.exception.UserNotFoundException;
import com.saham.hr_system.modules.employees.dto.EmployeeDetailsDto;
import com.saham.hr_system.modules.employees.dto.NewEmployeeDto;
import com.saham.hr_system.modules.employees.dto.SubordinateDetailsResponseDto;
import com.saham.hr_system.modules.employees.dto.UpdateEmployeeDto;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.service.implementation.EmployeeAdderServiceImpl;
import com.saham.hr_system.modules.employees.service.implementation.EmployeeQueryServiceImpl;
import com.saham.hr_system.modules.employees.service.implementation.EmployeeServiceImpl;
import com.saham.hr_system.modules.employees.service.implementation.EmployeeUpdateServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {


    private final Path profilePicturesPath;
    private final EmployeeServiceImpl employeeService;
    private final EmployeeAdderServiceImpl employeeAdderService;
    private final EmployeeQueryServiceImpl employeeQueryService;
    private final EmployeeUpdateServiceImpl employeeUpdateService;

    public EmployeeController(@Value("${file.upload.profile-pictures}") String path, EmployeeServiceImpl employeeService, EmployeeAdderServiceImpl employeeAdderService, EmployeeQueryServiceImpl employeeQueryService, EmployeeUpdateServiceImpl employeeUpdateService) {
        this.profilePicturesPath = Paths.get(path).toAbsolutePath().normalize();
        this.employeeService = employeeService;
        this.employeeAdderService = employeeAdderService;
        this.employeeQueryService = employeeQueryService;
        this.employeeUpdateService = employeeUpdateService;
    }
    /*
    @GetMapping("get")
    public ResponseEntity<?> getEmployee(@RequestParam String email){
        EmployeeDetailsDto employee = employeeService.getEmployeeDetails(email);

        return ResponseEntity
                .status(200)
                .body(employee);
    }

     */
    @GetMapping("profile/get")
    public ResponseEntity<Object> getEmployeeProfile(@RequestParam String email){
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
    @PatchMapping("update/{employeeId}")
    public ResponseEntity<?> updateEmployee(@PathVariable Long employeeId, @RequestBody UpdateEmployeeDto updateEmployeeDto){
        EmployeeDetailsDto response =
                employeeUpdateService.updateEmployee(employeeId, updateEmployeeDto);

        return ResponseEntity
                .status(200)
                .body(response);
    }

    @PatchMapping("update/profile-picture/{employeeId}")
    public ResponseEntity<Object> updateEmployeeProfilePicture(@PathVariable Long employeeId, @RequestBody MultipartFile multipartFile){
        return ResponseEntity
                .status(200)
                .body("Profile picture updated successfully");
    }

    @GetMapping("/profile/picture/{fileName}")
    public ResponseEntity<Resource> getEmployeeProfilePicture(@PathVariable String fileName) throws MalformedURLException {
        Path path = Paths.get(profilePicturesPath.toUri()).resolve(fileName);
        Resource resource = new UrlResource(path.toUri());
        return ResponseEntity.ok()
                .body(resource);
    }

    @GetMapping("get_all")
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

    @GetMapping("managers/get_all")
    public ResponseEntity<?> getAllManagers(){
        try{
            List<Employee> managers =
                    employeeQueryService.getAllManagers();
            return ResponseEntity
                    .status(200)
                    .body(managers);
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
