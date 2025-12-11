package com.saham.hr_system.modules.absence.utils;

import com.saham.hr_system.modules.absence.model.AbsenceRequest;
import com.saham.hr_system.modules.absence.repo.AbsenceRequestRepo;
import com.saham.hr_system.modules.employees.model.Employee;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Component
public class AbsenceReferenceNumberGenerator {
    private final static String PREFIX = "ABS";

    private final AbsenceRequestRepo absenceRequestRepo;
    public AbsenceReferenceNumberGenerator(AbsenceRequestRepo absenceRequestRepo) {
        this.absenceRequestRepo = absenceRequestRepo;
    }

    public String generate(AbsenceRequest absenceRequest) {
        Employee employee = absenceRequest.getEmployee();
        String fingerprint =
                Base64.getUrlEncoder()
                        .withoutPadding()
                        .encodeToString(employee.getEmail().getBytes())
                        .substring(0, 6);
        String todayPart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")); // the date of the today

        long employeeRequestCount = absenceRequestRepo.countAbsenceRequestByEmployee(
                employee
        );

        return
                String.format("%s%s%s%04d",PREFIX,fingerprint,todayPart,employeeRequestCount);
    }
}
