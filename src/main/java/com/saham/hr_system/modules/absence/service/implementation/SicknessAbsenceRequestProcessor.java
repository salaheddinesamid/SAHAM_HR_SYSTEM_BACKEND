package com.saham.hr_system.modules.absence.service.implementation;

import com.saham.hr_system.exception.UserNotFoundException;
import com.saham.hr_system.modules.absence.dto.AbsenceRequestDto;
import com.saham.hr_system.modules.absence.model.AbsenceRequest;
import com.saham.hr_system.modules.absence.model.AbsenceRequestStatus;
import com.saham.hr_system.modules.absence.model.AbsenceType;
import com.saham.hr_system.modules.absence.repo.AbsenceRequestRepo;
import com.saham.hr_system.modules.absence.service.AbsenceRequestProcessor;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.modules.leave.utils.TotalDaysCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SicknessAbsenceRequestProcessor implements AbsenceRequestProcessor {
    private final AbsenceRequestValidatorImpl absenceRequestValidator;
    private final EmployeeRepository employeeRepository;
    private final SicknessAbsenceDocumentStorageService sicknessAbsenceDocumentStorageService;
    private final AbsenceRequestRepo absenceRequestRepo;
    private final TotalDaysCalculator totalDaysCalculator;

    @Autowired
    public SicknessAbsenceRequestProcessor(AbsenceRequestValidatorImpl absenceRequestValidator, EmployeeRepository employeeRepository, SicknessAbsenceDocumentStorageService sicknessAbsenceDocumentStorageService, AbsenceRequestRepo absenceRequestRepo, TotalDaysCalculator totalDaysCalculator) {
        this.absenceRequestValidator = absenceRequestValidator;
        this.employeeRepository = employeeRepository;
        this.sicknessAbsenceDocumentStorageService = sicknessAbsenceDocumentStorageService;
        this.absenceRequestRepo = absenceRequestRepo;
        this.totalDaysCalculator = totalDaysCalculator;
    }

    @Override
    public boolean supports(String type) {
        return AbsenceType.SICKNESS.equals(AbsenceType.valueOf(type));
    }

    @Override
    public AbsenceRequest processAbsenceRequest(String email, AbsenceRequestDto requestDto) throws Exception {
        // validate the request:
        absenceRequestValidator.validate(requestDto);

        // fetch the employee from db:
        Employee employee =
                employeeRepository.findByEmail(email).orElseThrow(()-> new UserNotFoundException(email));

        double totalDays = totalDaysCalculator.calculateTotalDays(
                requestDto.getStartDate(),
                requestDto.getEndDate()
        );

        // process the request:
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

        // upload the medical document and save the PATH in the database:
        String medicalCertificatePath =  sicknessAbsenceDocumentStorageService.upload(employee.getFullName(), requestDto.getMedicalCertificate());

        // save the path:
        absenceRequest.setMedicalCertificatePath(medicalCertificatePath);

        // save the request
        return absenceRequestRepo.save(absenceRequest);
    }
}
