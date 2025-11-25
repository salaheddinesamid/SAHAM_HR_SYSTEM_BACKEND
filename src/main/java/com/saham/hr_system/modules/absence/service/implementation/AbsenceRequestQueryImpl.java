package com.saham.hr_system.modules.absence.service.implementation;

import com.saham.hr_system.exception.UserNotFoundException;
import com.saham.hr_system.modules.absence.dto.AbsenceRequestDetails;
import com.saham.hr_system.modules.absence.model.AbsenceRequest;
import com.saham.hr_system.modules.absence.repo.AbsenceRequestRepo;
import com.saham.hr_system.modules.absence.service.AbsenceRequestQuery;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AbsenceRequestQueryImpl implements AbsenceRequestQuery {

    private final EmployeeRepository employeeRepository;
    private final AbsenceRequestRepo absenceRequestRepo;

    @Autowired
    public AbsenceRequestQueryImpl(EmployeeRepository employeeRepository, AbsenceRequestRepo absenceRequestRepo) {
        this.employeeRepository = employeeRepository;
        this.absenceRequestRepo = absenceRequestRepo;
    }

    @Override
    public List<AbsenceRequestDetails> getAllMyAbsenceRequests(String email) {
        // fetch the employee from db:
        Employee employee =
                employeeRepository.findByEmail(email).orElseThrow(()-> new UserNotFoundException(email));
        List<AbsenceRequest> absenceRequests =
                absenceRequestRepo.findAllByEmployee(employee);

        return absenceRequests.stream().map(AbsenceRequestDetails::new).collect(Collectors.toList());
    }

    @Override
    public List<AbsenceRequestDetails> getAllSubordinateAbsenceRequests(String managerEmail) {
        // Fetch the manager:
        Employee manager =
                employeeRepository.findByEmail(managerEmail).orElseThrow(()-> new UserNotFoundException(managerEmail));

        // get the subordinates:
        List<Employee> subordinates =
                employeeRepository.findAllByManagerId(manager.getId());

        List<AbsenceRequest> absenceRequests =
                subordinates.stream().map(absenceRequestRepo::findAllByEmployee).flatMap(List::stream).toList();

        return absenceRequests
                .stream().map(AbsenceRequestDetails::new).collect(Collectors.toList());
    }
}
