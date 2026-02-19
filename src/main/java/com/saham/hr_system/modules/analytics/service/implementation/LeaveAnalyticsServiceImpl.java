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
import java.util.ArrayList;
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
        // Leaves
        List<Leave> leaves = leaveRepository.findAll();
        List<Leave> filteredLeaves = new ArrayList<>();
        // Leave Requests
        List<LeaveRequest> leaveRequests = leaveRequestRepository.findAll();

        List<LeaveRequest> filteredLeaveRequests = new ArrayList<>();
        // If the type is ALL, we don't filter by type, otherwise we filter by the specified type
        if(type.equals("ALL")){
            filteredLeaves = leaves;
        }
        if(!type.equals("ALL")){
            filteredLeaves = filterByType(leaves,type);
        }
        // If the from and to dates are provided, we filter by the date range
        if(from != null && to != null){
            filteredLeaves = filterLeavesByDateRange(filteredLeaves, from, to);
        }
        // If the department is not ALL, we filter by the specified department
        if(!department.equals("ALL")){
            filteredLeaves = filterLeavesByDepartment(filteredLeaves, department);
        }
        // If the entity is not ALL, we filter by the specified entity
        if(!entity.equals("ALL")){
            filteredLeaves = filterLeavesByEntity(filteredLeaves, entity);
        }

        long totalLeaves = filteredLeaves.size();
        return null;
    }

    private List<Leave> filterByType(List<Leave> leaves, String type) {
        return leaves.stream()
                .filter(leave -> leave.getLeaveType().toString().equals(type))
                .toList();
    }
    private List<Leave> filterLeavesByDateRange(List<Leave> leaves, LocalDate from, LocalDate to) {
        return leaves.stream()
                .filter(leave -> (leave.getFromDate().isEqual(from) || leave.getToDate().isAfter(from)) &&
                        (leave.getToDate().isEqual(to) || leave.getFromDate().isBefore(to)))
                .toList();
    }

    private List<Leave> filterLeavesByDepartment(List<Leave> leaves, String department) {
        return leaves.stream()
                .filter(leave -> leave.getEmployee().getEmployeeProfessionalDetails().getDepartment().toString().equals(department))
                .toList();
    }
    private List<Leave> filterLeavesByEntity(List<Leave> leaves, String entity) {
        return leaves.stream()
                .filter(leave -> leave.getEmployee().getEmployeeProfessionalDetails().getEntity().toString().equals(entity))
                .toList();
    }
}
