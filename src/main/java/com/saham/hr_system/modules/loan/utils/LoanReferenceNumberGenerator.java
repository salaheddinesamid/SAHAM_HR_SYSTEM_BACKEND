package com.saham.hr_system.modules.loan.utils;

import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.loan.model.LoanRequest;
import com.saham.hr_system.modules.loan.repository.LoanRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Component
public class LoanReferenceNumberGenerator {

    private final static String PREFIX = "LOAN";
    private final LoanRequestRepository loanRequestRepository;

    @Autowired
    public LoanReferenceNumberGenerator(LoanRequestRepository loanRequestRepository) {
        this.loanRequestRepository = loanRequestRepository;
    }

    public String generate(LoanRequest loanRequest) {
        Employee employee = loanRequest.getEmployee();
        String fingerprint =
                Base64
                        .getUrlEncoder()
                        .withoutPadding()
                        .encodeToString(employee.getEmail().getBytes())
                        .substring(0, 6);
        String todayPart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")); // the date of the today

        long employeeRequestCount = loanRequestRepository.countByEmployee(
                employee
        );

        return
                String.format("%s%s%s%04d",PREFIX,fingerprint,todayPart,employeeRequestCount);
    }
}
