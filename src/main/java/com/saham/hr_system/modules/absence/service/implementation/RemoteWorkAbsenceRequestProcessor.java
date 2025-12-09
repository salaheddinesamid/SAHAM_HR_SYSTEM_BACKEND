package com.saham.hr_system.modules.absence.service.implementation;

import com.saham.hr_system.exception.UserNotFoundException;
import com.saham.hr_system.modules.absence.dto.AbsenceRequestDto;
import com.saham.hr_system.modules.absence.model.AbsenceRequest;
import com.saham.hr_system.modules.absence.model.AbsenceType;
import com.saham.hr_system.modules.absence.repo.AbsenceRequestRepo;
import com.saham.hr_system.modules.absence.service.AbsenceRequestProcessor;
import com.saham.hr_system.modules.absence.utils.AbsenceReferenceNumberGenerator;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.utils.TotalDaysCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * Processor implementation responsible for handling remote-work absence requests.
 * <p>
 * This class validates the request, fetches the employee, maps the DTO to an entity,
 * calculates the total days, assigns a reference number, triggers email notifications,
 * and finally persists the absence request in the database.
 */
@Component
public class RemoteWorkAbsenceRequestProcessor implements AbsenceRequestProcessor {

    private final EmployeeRepository employeeRepository;
    private final AbsenceRequestValidatorImpl absenceRequestValidator;
    private final AbsenceRequestMapperImpl absenceMapper;
    private final AbsenceRequestRepo absenceRequestRepository;
    private final TotalDaysCalculator absenceTotalDaysCalculator;
    private final AbsenceReferenceNumberGenerator absenceReferenceNumberGenerator;
    private final AbsenceRequestEmailSenderImpl absenceRequestEmailSender;

    /**
     * Constructs a new RemoteWorkAbsenceRequestProcessor with required dependencies.
     *
     * @param employeeRepository               repository for fetching employee information
     * @param absenceRequestValidator          validator for validating absence request input
     * @param absenceMapper                    mapper to convert DTOs into AbsenceRequest entities
     * @param absenceRequestRepository         repository for persisting absence requests
     * @param absenceTotalDaysCalculator       utility for calculating total requested absence days
     * @param absenceReferenceNumberGenerator  utility for generating unique reference numbers
     * @param absenceRequestEmailSender        component responsible for sending email notifications
     */
    @Autowired
    public RemoteWorkAbsenceRequestProcessor(EmployeeRepository employeeRepository,
                                             AbsenceRequestValidatorImpl absenceRequestValidator,
                                             AbsenceRequestMapperImpl absenceMapper,
                                             AbsenceRequestRepo absenceRequestRepository,
                                             TotalDaysCalculator absenceTotalDaysCalculator,
                                             AbsenceReferenceNumberGenerator absenceReferenceNumberGenerator,
                                             AbsenceRequestEmailSenderImpl absenceRequestEmailSender) {
        this.employeeRepository = employeeRepository;
        this.absenceRequestValidator = absenceRequestValidator;
        this.absenceMapper = absenceMapper;
        this.absenceRequestRepository = absenceRequestRepository;
        this.absenceTotalDaysCalculator = absenceTotalDaysCalculator;
        this.absenceReferenceNumberGenerator = absenceReferenceNumberGenerator;
        this.absenceRequestEmailSender = absenceRequestEmailSender;
    }

    /**
     * Determines whether this processor supports handling the given absence type.
     *
     * @param type the absence type as a string
     * @return true if the type is REMOTE_WORK, false otherwise
     */
    @Override
    public boolean supports(String type) {
        return AbsenceType.REMOTE_WORK.equals(AbsenceType.valueOf(type));
    }

    /**
     * Processes a remote-work absence request submitted by an employee.
     * <p>
     * The method performs the following steps:
     * <ol>
     *     <li>Validates the input request DTO</li>
     *     <li>Fetches the employee from the database</li>
     *     <li>Maps the DTO to an AbsenceRequest entity</li>
     *     <li>Calculates the total number of absence days</li>
     *     <li>Generates and assigns a reference number</li>
     *     <li>Sends asynchronous email notifications</li>
     *     <li>Persists the absence request to the database</li>
     * </ol>
     *
     * @param email      the email of the employee submitting the request
     * @param requestDto the absence request payload
     * @return the saved AbsenceRequest entity
     * @throws Exception if validation fails or employee is not found
     */
    @Override
    public AbsenceRequest processAbsenceRequest(String email, AbsenceRequestDto requestDto) throws Exception {
        // validate the request:
        absenceRequestValidator.validate(requestDto);

        // fetch employee from db:
        Employee employee = employeeRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        // create new absence:
        AbsenceRequest absenceRequest = absenceMapper.mapToEntity(requestDto);

        // calculate the total days:
        long totalDays = absenceTotalDaysCalculator.calculateTotalDays(
                requestDto.getStartDate(), requestDto.getEndDate()
        );

        assert absenceRequest != null;
        absenceRequest.setTotalDays(totalDays);
        absenceRequest.setEmployee(employee);

        // generate and assign reference number:
        String refNumber = absenceReferenceNumberGenerator.generate(absenceRequest);
        absenceRequest.setReferenceNumber(refNumber);

        // asynchronous email notifications:
        CompletableFuture.runAsync(() -> {
            try {
                absenceRequestEmailSender.notifyEmployee(absenceRequest);
                absenceRequestEmailSender.notifyManager(absenceRequest);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        // save to database:
        return absenceRequestRepository.save(absenceRequest);
    }
}
