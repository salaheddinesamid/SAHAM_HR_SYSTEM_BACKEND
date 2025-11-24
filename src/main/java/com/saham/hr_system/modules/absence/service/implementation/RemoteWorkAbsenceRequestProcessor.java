package com.saham.hr_system.modules.absence.service.implementation;

import com.saham.hr_system.exception.UserNotFoundException;
import com.saham.hr_system.modules.absence.dto.AbsenceRequestDto;
import com.saham.hr_system.modules.absence.model.AbsenceRequest;
import com.saham.hr_system.modules.absence.model.AbsenceType;
import com.saham.hr_system.modules.absence.repo.AbsenceRequestRepo;
import com.saham.hr_system.modules.absence.service.AbsenceRequestProcessor;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RemoteWorkAbsenceRequestProcessor implements AbsenceRequestProcessor {

    private final EmployeeRepository employeeRepository;
    private final AbsenceRequestValidatorImpl absenceRequestValidator;
    private final AbsenceRequestMapperImpl absenceMapper;
    private final AbsenceRequestRepo absenceRequestRepository;

    @Autowired
    public RemoteWorkAbsenceRequestProcessor(EmployeeRepository employeeRepository, AbsenceRequestValidatorImpl absenceRequestValidator, AbsenceRequestMapperImpl absenceMapper, AbsenceRequestRepo absenceRequestRepository) {
        this.employeeRepository = employeeRepository;
        this.absenceRequestValidator = absenceRequestValidator;
        this.absenceMapper = absenceMapper;
        this.absenceRequestRepository = absenceRequestRepository;
    }

    @Override
    public boolean supports(String type) {
        return AbsenceType.REMOTE_WORK.equals(AbsenceType.valueOf(type));
    }

    @Override
    public AbsenceRequest processAbsenceRequest(String email, AbsenceRequestDto requestDto) throws Exception {
        // validate the request:
        absenceRequestValidator.validate(requestDto);

        // fetch employee from db:
        Employee employee = employeeRepository
                .findByEmail(email).orElseThrow(()-> new UserNotFoundException(email));

        // create new absence:
        AbsenceRequest absenceRequest = absenceMapper.mapToEntity(requestDto);
        assert absenceRequest != null;
        absenceRequest.setEmployee(employee);

        // save the absence to db:
        return absenceRequestRepository.save(absenceRequest);

    }
}
