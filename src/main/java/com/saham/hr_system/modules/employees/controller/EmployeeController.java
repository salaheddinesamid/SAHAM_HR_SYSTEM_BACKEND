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
/** * This controller handles all employee-related operations, including:
 * - Retrieving employee details and profiles
 * - Adding new employees
 * - Updating employee information and passwords
 * - Managing employee profile pictures
 * - Fetching lists of employees and managers
 */
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
    /**
     * This endpoint retrieves the profile details of an employee based on their email.
     * It returns a 200 OK response with the employee details if found, or a 404 Not Found if the employee does not exist.
     * @param email The email of the employee whose profile is being requested.
     * @return ResponseEntity containing the employee details or an error message.
     */
    @GetMapping("profile/get")
    public ResponseEntity<Object> getEmployeeProfile(@RequestParam String email){
        EmployeeDetailsDto employee = employeeService.getEmployeeDetails(email);

        return ResponseEntity
                .status(200)
                .body(employee);
    }
    /**
     * This endpoint allows for the creation of a new employee in the system. It accepts a JSON payload containing the new employee's details, including personal information, professional details, social details, and contact details. The endpoint validates the input data and creates a new employee record in the database. If the employee is successfully created, it returns a 200 OK response with the details of the newly created employee. If an employee with the same email already exists, it throws an EmployeeAlreadyExistsException.
     * @param newEmployee A JSON object containing the new employee's details.
     * @return ResponseEntity containing the details of the newly created employee or an error message if the employee already exists.
     */
    @PostMapping("new")
    public ResponseEntity<?> newEmployee(@RequestBody NewEmployeeDto newEmployee){
        EmployeeDetailsDto response = employeeAdderService
                .add(newEmployee);

        return ResponseEntity
                .status(200)
                .body(response);
    }
    /**
     * This endpoint allows for updating an existing employee's information. It accepts the employee's ID as a path variable and a JSON payload containing the updated details. The endpoint validates the input data and updates the corresponding employee record in the database. If the update is successful, it returns a 200 OK response with the updated employee details. If the employee with the specified ID does not exist, it throws a UserNotFoundException.
     * @param employeeId The ID of the employee to be updated.
     * @param updateEmployeeDto A JSON object containing the updated details of the employee.
     * @return ResponseEntity containing the updated employee details or an error message if the employee is not found.
     */
    @PatchMapping("update/{employeeId}")
    public ResponseEntity<?> updateEmployee(@PathVariable Long employeeId, @RequestBody UpdateEmployeeDto updateEmployeeDto){
        EmployeeDetailsDto response =
                employeeUpdateService.updateEmployee(employeeId, updateEmployeeDto);

        return ResponseEntity
                .status(200)
                .body(response);
    }

    /**
     * This endpoint allows an employee to update their password. It accepts the employee's email as a request parameter and a JSON payload containing the current password and the new password. The endpoint validates the input data, checks if the current password is correct, and updates the password in the database. If the update is successful, it returns a 200 OK response with no content. If the employee with the specified email does not exist or if the current password is incorrect, it throws a UserNotFoundException or an InvalidPasswordException, respectively.
     * @param email The email of the employee whose password is being updated.
     * @param passwordUpdateDto A JSON object containing the current password and the new password.
     * @return ResponseEntity with no content if the update is successful or an error message if the employee is not found or if the current password is incorrect.
     */
    @PatchMapping("update/password")
    public ResponseEntity<Object> updateEmployeePassword(
            @RequestParam String email,
            @RequestBody PasswordUpdateDto passwordUpdateDto
            ){
        employeePasswordUpdateService.updatePassword(email, passwordUpdateDto);
        return ResponseEntity.ok().build();
    }

    /**
     * This endpoint allows an employee to update their profile picture. It accepts a multipart file containing the new profile picture and the employee's email as a request parameter. The endpoint validates the input file, checks if it is of an allowed type (JPEG, PNG, WEBP) and within the size limit (5MB), and updates the profile picture in the database. If the update is successful, it returns a 200 OK response with a success message. If no file is provided, if the file type is invalid, if the file size exceeds the limit, or if the employee with the specified email does not exist, it returns an appropriate error response with a corresponding status code and error message.
     * @param image The multipart file containing the new profile picture.
     * @return ResponseEntity containing a success message if the update is successful or an error message if there are issues with the file or if the employee is not found.
     */
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

    /**
     * This endpoint allows an employee to retrieve their profile picture. It accepts the employee's user ID and the picture path as request parameters. The endpoint checks if the authenticated user has permission to access the requested profile picture by comparing the user ID with the authenticated user's ID. If the user has permission, it retrieves the profile picture from the specified path and returns it as a resource in a 200 OK response. If the user does not have permission, it returns a 403 Forbidden response with an appropriate error message. If there are issues with retrieving the profile picture, such as a malformed URL, it throws a MalformedURLException.
     * @param userId The ID of the employee whose profile picture is being requested.
     * @param picturePath The path to the profile picture file.
     * @return ResponseEntity containing the profile picture resource if access is granted or an error message if access is denied or if there are issues with retrieving the picture.
     * @throws MalformedURLException if there is an issue with the URL of the profile picture.
     */
    @GetMapping("/profile-picture")
    public ResponseEntity<Object> getEmployeeProfilePicture(@RequestParam long userId ,@RequestParam String picturePath) throws MalformedURLException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // find the employee to ensure they exist and have access to the picture:
        Employee employee = employeeRepository
                .findByEmail(email).orElseThrow(()-> new UserNotFoundException("Employee not found with email: " + email));
        if(employee.getId() != userId){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    Map.of("error", "You do not have permission to access this profile picture")
            );
        }
        Path path = Paths.get(profilePicturesPath.toUri()).resolve(picturePath);
        Resource resource = new UrlResource(path.toUri());
        return ResponseEntity.ok()
                .body(resource);
    }
    /**
     * This endpoint retrieves a paginated list of all employees in the system. It accepts optional request parameters for page number and page size, which default to 0 and 5, respectively. The endpoint returns a 200 OK response with a page of EmployeeDetailsDto objects containing the details of the employees. If there are no employees found, it returns an empty page.
     * @param pageNumber The page number to retrieve (default is 0).
     * @param pageSize The number of employees to include in each page (default is 5).
     * @return ResponseEntity containing a page of EmployeeDetailsDto objects or an empty page if no employees are found.
     */
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
    /**
     * This endpoint retrieves the leave balances of all employees in the system. It returns a 200 OK response with a list of EmployeeBalanceDto objects containing the leave balance details for each employee. If there are no employees found, it returns an empty list.
     * @return ResponseEntity containing a list of EmployeeBalanceDto objects or an empty list if no employees are found.
     */
    @GetMapping("/balances/get_all")
    public ResponseEntity<Object> getAllEmployeeBalances(){
        return ResponseEntity
                .status(200)
                .body(employeeQueryService.getAllEmployeesBalances());
    }
    /**
     * This endpoint verifies if a manager with the specified full name exists in the system. It accepts the full name of the manager as a request parameter and returns a 200 OK response with a boolean value indicating whether the manager exists or not. If there are any issues during the verification process, it throws an appropriate exception.
     * @param fullName The full name of the manager to verify.
     * @return ResponseEntity containing a boolean value indicating whether the manager exists or not.
     */
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
    /**
     * This endpoint retrieves a list of all managers in the system. It returns a 200 OK response with a list of Employee objects representing the managers. If there are no managers found, it returns an empty list. If there are any issues during the retrieval process, it throws a UserNotFoundException.
     * @return ResponseEntity containing a list of Employee objects representing the managers or an empty list if no managers are found.
     */
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
    /**
     * This endpoint retrieves a list of subordinates for a given manager based on the manager's email. It accepts the manager's email as a request parameter and returns a 200 OK response with a list of SubordinateDetailsResponseDto objects containing the details of the subordinates. If there are no subordinates found for the specified manager, it returns an empty list. If there are any issues during the retrieval process, it throws an appropriate exception.
     * @param managerEmail The email of the manager whose subordinates are being requested.
     * @return ResponseEntity containing a list of SubordinateDetailsResponseDto objects or an empty list if no subordinates are found.
     */
    @GetMapping("/subordinates")
    public ResponseEntity<?> getSubordinates(@RequestParam String managerEmail){
        List<SubordinateDetailsResponseDto> response =
                employeeService.getSubordinates(managerEmail);

        return ResponseEntity
                .status(200)
                .body(response);
    }
}
