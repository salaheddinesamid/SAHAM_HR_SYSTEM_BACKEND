package com.saham.hr_system.modules.absence.utils;

import com.saham.hr_system.modules.absence.model.AbsenceRequest;
import com.saham.hr_system.modules.absence.repo.AbsenceRequestRepo;
import com.saham.hr_system.modules.employees.model.Employee;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class AbsenceReferenceNumberGenerator {
    private final AbsenceRequestRepo absenceRequestRepo;

    public AbsenceReferenceNumberGenerator(AbsenceRequestRepo absenceRequestRepo) {
        this.absenceRequestRepo = absenceRequestRepo;
    }

    public String generate(AbsenceRequest absenceRequest) {
        Employee employee = absenceRequest.getEmployee();
        String prefix = "SHDA"; // SAHAM HR ABSENCE REQUEST Abbreviation
        String employeeMatriculationNumber = employee.getMatriculation(); // Employee's Matriculation Number
        String todayPart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")); // the date of the today

        long employeeRequestCount = absenceRequestRepo.countAbsenceRequestByEmployee(
                employee
        );

        return
                String.format("%s-%s-%s-%04d",prefix,employeeMatriculationNumber,todayPart,employeeRequestCount);
    }
}
