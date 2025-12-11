package com.saham.hr_system.modules.leave.utils;

import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.leave.model.LeaveRequest;
import com.saham.hr_system.modules.leave.repository.LeaveRequestRepository;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Component
public class LeaveRequestRefNumberGenerator {
    private final LeaveRequestRepository leaveRequestRepository;

    public LeaveRequestRefNumberGenerator(LeaveRequestRepository leaveRequestRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
    }
    public String generate(LeaveRequest leaveRequest) {
        Employee employee = leaveRequest.getEmployee();
        String prefix = "LEAVE"; // LEAVE REQUEST Abbreviation
        String fingerprint =
                Base64.getUrlEncoder()
                        .withoutPadding()
                        .encodeToString(employee.getEmail().getBytes())
                        .substring(0, 6);
        String todayPart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")); // the date of the today

        long employeeRequestCount = leaveRequestRepository.countLeaveRequestByEmployee(
                employee
        );

        return
                String.format("%s%s%s%04d",prefix,fingerprint,todayPart,employeeRequestCount);
    }
}
