package com.saham.hr_system.modules.analytics.service.implementation;

import com.saham.hr_system.modules.analytics.dto.LeaveAnalyticsDto;
import com.saham.hr_system.modules.analytics.service.LeaveAnalyticsService;
import com.saham.hr_system.modules.leave.model.Leave;
import com.saham.hr_system.modules.leave.model.LeaveRequest;
import com.saham.hr_system.modules.leave.repository.LeaveRepository;
import com.saham.hr_system.modules.leave.repository.LeaveRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LeaveAnalyticsServiceImpl implements LeaveAnalyticsService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveRepository leaveRepository;
    @Autowired
    public LeaveAnalyticsServiceImpl(LeaveRequestRepository leaveRequestRepository, LeaveRepository leaveRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.leaveRepository = leaveRepository;
    }

    @Override
    public LeaveAnalyticsDto getLeaveAnalyticsOverview(String type, LocalDate from, LocalDate to, String department, String entity) {
        List<Leave> leaves = leaveRepository.findAll();
        /* TODO: Implement the logic to filter the leaves based on the provided parameters (type, from, to, department, entity) and calculate the analytics data. */
        return null;
    }
}
