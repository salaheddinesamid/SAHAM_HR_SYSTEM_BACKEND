package com.saham.hr_system.modules.absence.service.implementation;

import com.saham.hr_system.exception.UserNotFoundException;
import com.saham.hr_system.modules.absence.dto.AbsenceRequestDto;
import com.saham.hr_system.modules.absence.model.AbsenceRequest;
import com.saham.hr_system.modules.absence.model.AbsenceRequestStatus;
import com.saham.hr_system.modules.absence.model.AbsenceType;
import com.saham.hr_system.modules.absence.repo.AbsenceRequestRepo;
import com.saham.hr_system.modules.absence.service.AbsenceRequestProcessor;
import com.saham.hr_system.modules.absence.utils.AbsenceReferenceNumberGenerator;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.utils.TotalDaysCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

/**
 * Implementation of {@link AbsenceRequestProcessor} that handles sickness-related absence requests.
 * <p>
 * This processor is responsible for validating the request, ensuring that a medical certificate is
 * provided, storing the uploaded certificate file, computing the total number of absence days,
 * generating a unique reference number, and finally saving the constructed {@link AbsenceRequest}
 * to the database.
 * </p>
 * <p>
 * Notifications to the employee and their manager are performed asynchronously to avoid blocking
 * the main request-processing thread.
 * </p>
 */
@Component
public class SicknessAbsenceRequestProcessor implements AbsenceRequestProcessor {

    private final AbsenceRequestValidatorImpl absenceRequestValidator;
    private final EmployeeRepository employeeRepository;
    private final SicknessAbsenceDocumentStorageService sicknessAbsenceDocumentStorageService;
    private final AbsenceRequestRepo absenceRequestRepo;
    private final TotalDaysCalculator totalDaysCalculator;
    private final AbsenceReferenceNumberGenerator absenceReferenceNumberGenerator;
    private final AbsenceRequestEmailSenderImpl absenceRequestEmailSender;

    /**
     * Constructs the processor with all required service dependencies.
     */
    @Autowired
    public SicknessAbsenceRequestProcessor(
            AbsenceRequestValidatorImpl absenceRequestValidator,
            EmployeeRepository employeeRepository,
            SicknessAbsenceDocumentStorageService sicknessAbsenceDocumentStorageService,
            AbsenceRequestRepo absenceRequestRepo,
            TotalDaysCalculator totalDaysCalculator,
            AbsenceReferenceNumberGenerator absenceReferenceNumberGenerator,
            AbsenceRequestEmailSenderImpl absenceRequestEmailSender) {

        this.absenceRequestValidator = absenceRequestValidator;
        this.employeeRepository = employeeRepository;
        this.sicknessAbsenceDocumentStorageService = sicknessAbsenceDocumentStorageService;
        this.absenceRequestRepo = absenceRequestRepo;
        this.totalDaysCalculator = totalDaysCalculator;
        this.absenceReferenceNumberGenerator = absenceReferenceNumberGenerator;
        this.absenceRequestEmailSender = absenceRequestEmailSender;
    }

    /**
     * Returns {@code true} if the supplied absence type corresponds to sickness.
     */
    @Override
    public boolean supports(String type) {
        return AbsenceType.SICKNESS.equals(AbsenceType.valueOf(type));
    }

    /**
     * Processes the sickness absence request by applying validation rules,
     * storing the medical certificate, generating a reference number,
     * computing the total number of days, and saving the new {@link AbsenceRequest}.
     * <p>
     * After saving the request, this method also triggers asynchronous email
     * notifications to the employee and their manager.
     * </p>
     */
    @Override
    public AbsenceRequest processAbsenceRequest(String email, AbsenceRequestDto requestDto) throws Exception {

        // Validate request fields (date ranges, required attachments, etc.)
        absenceRequestValidator.validate(requestDto);

        // Fetch the employee making the request
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        // Compute total absence duration
        double totalDays = totalDaysCalculator.calculateTotalDays(
                requestDto.getStartDate(),
                requestDto.getEndDate()
        );

        // Build the new absence request entity
        AbsenceRequest absenceRequest = new AbsenceRequest();
        absenceRequest.setIssueDate(LocalDateTime.now());
        absenceRequest.setEmployee(employee);
        absenceRequest.setType(AbsenceType.SICKNESS);
        absenceRequest.setStartDate(requestDto.getStartDate());
        absenceRequest.setEndDate(requestDto.getEndDate());
        absenceRequest.setStatus(AbsenceRequestStatus.IN_PROCESS);
        absenceRequest.setApprovedByManager(false);
        absenceRequest.setApprovedByHr(false);
        absenceRequest.setTotalDays(totalDays);

        // Generate and assign a unique reference number
        String referenceNumber = absenceReferenceNumberGenerator.generate(absenceRequest);
        absenceRequest.setReferenceNumber(referenceNumber);

        // Upload the medical certificate and store its path
        String medicalCertificatePath =
                sicknessAbsenceDocumentStorageService.upload(
                        employee.getFullName(),
                        requestDto.getMedicalCertificate()
                );
        absenceRequest.setMedicalCertificatePath(medicalCertificatePath);

        // Send notifications asynchronously
        CompletableFuture.runAsync(() -> {
            try {
                absenceRequestEmailSender.notifyEmployee(absenceRequest);
                absenceRequestEmailSender.notifyManager(absenceRequest);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        // Save and return the newly created absence request
        return absenceRequestRepo.save(absenceRequest);
    }
}
