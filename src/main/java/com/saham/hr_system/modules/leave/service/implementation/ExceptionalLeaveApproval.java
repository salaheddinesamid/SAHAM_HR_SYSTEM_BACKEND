package com.saham.hr_system.modules.leave.service.implementation;

import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.leave.model.Leave;
import com.saham.hr_system.modules.leave.model.LeaveRequest;
import com.saham.hr_system.modules.leave.model.LeaveType;
import com.saham.hr_system.modules.leave.repository.LeaveRepository;
import com.saham.hr_system.modules.leave.repository.LeaveRequestRepository;
import com.saham.hr_system.modules.leave.service.LeaveApproval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExceptionalLeaveApproval implements LeaveApproval {

    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveRepository leaveRepository;
    @Autowired
    public ExceptionalLeaveApproval(LeaveRequestRepository leaveRequestRepository, LeaveRepository leaveRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.leaveRepository = leaveRepository;
    }

    @Override
    public boolean supports(String leaveType) {
        return LeaveType.EXCEPTIONAL.equals(LeaveType.valueOf(leaveType));
    }

    /**
     * This function approve leave request by HR, without any balance reduction
     * @param requestId
     * @return a leave
     */
    @Override
    public Leave approve(Long requestId) {
        // Fetch the request:
        LeaveRequest leaveRequest =
                leaveRequestRepository.findById(requestId).orElseThrow();


        // Get the employee:
        Employee employee  = leaveRequest.getEmployee();
        double totalDays =
                leaveRequest.getTotalDays();

        // create new leave:
        Leave leave = new Leave();
        leave.setEmployee(employee);
        leave.setLeaveType(LeaveType.ANNUAL);
        leave.setFromDate(leaveRequest.getStartDate());
        leave.setToDate(leaveRequest.getEndDate());
        leave.setTotalDays(totalDays);

        return leaveRepository.save(leave);
    }

    @Override
    public void approveSubordinate(Long requestId) {

    }

    @Override
    public void rejectSubordinate(Long requestId) {

    }

    @Override
    public void rejectLeave(Long requestId) {

    }
}
