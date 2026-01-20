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
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 *
 */
@Service
public class MaternityAbsenceRequestProcessor implements AbsenceRequestProcessor {
    private final AbsenceRequestRepo absenceRequestRepo;
    private final EmployeeRepository employeeRepository;
    private final AbsenceRequestValidatorImpl absenceRequestValidator;
    private final AbsenceRequestMapperImpl absenceRequestMapper;
    private final TotalDaysCalculator absenceTotalDaysCalculator;
    private final AbsenceReferenceNumberGenerator absenceReferenceNumberGenerator;
    private final AbsenceRequestEmailSenderImpl absenceRequestEmailSender;
    @Autowired
    public MaternityAbsenceRequestProcessor(AbsenceRequestRepo absenceRequestRepo, EmployeeRepository employeeRepository, AbsenceRequestValidatorImpl absenceRequestValidator, AbsenceRequestMapperImpl absenceRequestMapper, TotalDaysCalculator absenceTotalDaysCalculator, AbsenceReferenceNumberGenerator absenceReferenceNumberGenerator, AbsenceRequestEmailSenderImpl absenceRequestEmailSender) {
        this.absenceRequestRepo = absenceRequestRepo;
        this.employeeRepository = employeeRepository;
        this.absenceRequestValidator = absenceRequestValidator;
        this.absenceRequestMapper = absenceRequestMapper;
        this.absenceTotalDaysCalculator = absenceTotalDaysCalculator;
        this.absenceReferenceNumberGenerator = absenceReferenceNumberGenerator;
        this.absenceRequestEmailSender = absenceRequestEmailSender;
    }

    @Override
    public boolean supports(String type) {
        return AbsenceType.MATERNITY.equals(AbsenceType.valueOf(type));
    }

    @Override
    public AbsenceRequest processAbsenceRequest(String email, AbsenceRequestDto requestDto) throws Exception {
        // validate the request:
        absenceRequestValidator.validate(requestDto);

        // fetch the employee from db:
        Employee employee = employeeRepository
                .findByEmail(email).orElseThrow(()-> new UserNotFoundException(email));

        // create new absence:
        AbsenceRequest absenceRequest = absenceRequestMapper.mapToEntity(requestDto);

        // calculate the total days:
        long totalDays = absenceTotalDaysCalculator.calculateTotalDays(requestDto.getStartDate(), requestDto.getEndDate());

        assert absenceRequest != null;
        absenceRequest.setTotalDays(totalDays);
        absenceRequest.setEmployee(employee);

        // generate and assign reference number:
        String refNumber = absenceReferenceNumberGenerator.generate(absenceRequest);
        absenceRequest.setReferenceNumber(refNumber);
        // save the request:
        AbsenceRequest savedAbsenceRequest = absenceRequestRepo.save(absenceRequest);
        // asynchronous email notification:
        CompletableFuture.runAsync(()-> {
            try{
                // notify employee:
                absenceRequestEmailSender.notifyEmployee(savedAbsenceRequest);
                // notify manager:
                absenceRequestEmailSender.notifyManager(savedAbsenceRequest);
            }catch (Exception ex){
                throw new RuntimeException(ex);
            }
        });
        // save the request to the database:
        return savedAbsenceRequest;

    }
}
