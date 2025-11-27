package com.saham.hr_system.modules.leave.utils;

import com.saham.hr_system.modules.leave.repository.LeaveRequestRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class LeaveRequestRefNumberGenerator {
    private final LeaveRequestRepository leaveRequestRepository;

    public LeaveRequestRefNumberGenerator(LeaveRequestRepository leaveRequestRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
    }
    public String generate(){
        String prefix = "SHCO";
        String todayPart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        LocalDateTime startDay = LocalDate.now().atStartOfDay();
        LocalDateTime endDay = startDay.plusDays(1);

        long count = leaveRequestRepository
                .countByRequestDateBetween(startDay, endDay) + 1;

        return prefix + "-" + todayPart + "-" + String.format("%04d", count);
    }
}
