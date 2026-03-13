package com.saham.hr_system.modules.employees.controller;

import com.saham.hr_system.exception.UserNotFoundException;
import com.saham.hr_system.modules.employees.dto.*;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.modules.employees.service.implementation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {


    private final Path profilePicturesPath;
    private final EmployeeServiceImpl employeeService;
    private final EmployeeAdderServiceImpl employeeAdderService;
    private final EmployeeQueryServiceImpl employeeQueryService;
    private final EmployeeUpdateServiceImpl employeeUpdateService;
    private final EmployeePasswordUpdateServiceImpl employeePasswordUpdateService;
    private final EmployeeRepository employeeRepository;

    public EmployeeController(@Value("${file.upload.profile-pictures}") String path, EmployeeServiceImpl employeeService, EmployeeAdderServiceImpl employeeAdderService, EmployeeQueryServiceImpl employeeQueryService, EmployeeUpdateServiceImpl employeeUpdateService, EmployeePasswordUpdateServiceImpl employeePasswordUpdateService, EmployeeRepository employeeRepository) {
        this.profilePicturesPath = Paths.get(path).toAbsolutePath().normalize();
        this.employeeService = employeeService;
        this.employeeAdderService = employeeAdderService;
        this.employeeQueryService = employeeQueryService;
        this.employeeUpdateService = employeeUpdateService;
        this.employeePasswordUpdateService = employeePasswordUpdateService;
        this.employeeRepository = employeeRepository;
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

    @PatchMapping("update/password")
    public ResponseEntity<Object> updateEmployeePassword(
            @RequestParam String email,
            @RequestBody PasswordUpdateDto passwordUpdateDto
            ){
        employeePasswordUpdateService.updatePassword(email, passwordUpdateDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/profile-picture")
    public ResponseEntity<Map<String, String>> updateEmployeeProfilePicture(
            @RequestParam("image") MultipartFile image
    ) {
        // Ensure the image is not empty
        if (image == null || image.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "No image file provided"));
        }

        List<String> allowedTypes = List.of("image/jpeg", "image/png", "image/webp");
        if (!allowedTypes.contains(image.getContentType())) {
            return ResponseEntity
                    .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                    .body(Map.of("error", "Invalid file type. Allowed: JPEG, PNG, WEBP"));
        }

        if (image.getSize() > 5 * 1024 * 1024) {
            return ResponseEntity
                    .status(HttpStatus.PAYLOAD_TOO_LARGE)
                    .body(Map.of("error", "File size exceeds the 5MB limit"));
        }

        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            employeeUpdateService.updateEmployeeProfilePicture(email, image);
            return ResponseEntity
                    .ok(Map.of("message", "Profile picture updated successfully"));

        } catch (UserNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));

        }
    }

    @GetMapping("/profile-picture")
    public ResponseEntity<Object> getEmployeeProfilePicture(@RequestParam long id ,@RequestParam String picturePath) throws MalformedURLException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // find the employee to ensure they exist and have access to the picture:
        Employee employee = employeeRepository
                .findByEmail(email).orElseThrow(()-> new UserNotFoundException("Employee not found with email: " + email));
        if(employee.getId() != id){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    Map.of("error", "You do not have permission to access this profile picture")
            );
        }
        Path path = Paths.get(profilePicturesPath.toUri()).resolve(picturePath);
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
    @GetMapping("/balances/get_all")
    public ResponseEntity<Object> getAllEmployeeBalances(){
        return ResponseEntity
                .status(200)
                .body(employeeQueryService.getAllEmployeesBalances());
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
