package com.saham.hr_system.modules.leave.service.implementation;

import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.leave.model.Leave;
import com.saham.hr_system.modules.leave.model.LeaveRequest;
import com.saham.hr_system.modules.leave.model.LeaveRequestStatus;
import com.saham.hr_system.modules.leave.model.LeaveType;
import com.saham.hr_system.modules.leave.repository.LeaveRepository;
import com.saham.hr_system.modules.leave.repository.LeaveRequestRepository;
import com.saham.hr_system.modules.leave.service.LeaveApproval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class ExceptionalLeaveApproval implements LeaveApproval {

    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveRepository leaveRepository;
    private final LeaveApprovalEmailSenderImpl leaveApprovalEmailSender;
    @Autowired
    public ExceptionalLeaveApproval(LeaveRequestRepository leaveRequestRepository, LeaveRepository leaveRepository, LeaveApprovalEmailSenderImpl leaveApprovalEmailSender) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.leaveRepository = leaveRepository;
        this.leaveApprovalEmailSender = leaveApprovalEmailSender;
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

        // notify the employee:
        CompletableFuture.runAsync(()->{
            try {
                leaveApprovalEmailSender.sendHRApprovalEmailToEmployee(leaveRequest);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        // notify the manager:
        CompletableFuture.runAsync(()->{
            try {
                leaveApprovalEmailSender.sendHRApprovalEmailToManager(leaveRequest);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void approveSubordinate(String approvedBy,Long requestId) {
        // fetch the request from db:
        LeaveRequest leaveRequest =leaveRequestRepository.findById(requestId).orElseThrow();

        // set the request:
        leaveRequest.setApprovedByManager(true);

        // save the request:
        leaveRequestRepository.save(leaveRequest);
        // notify the employee:
        // notify the HR:
    }

    @Override
    public void rejectSubordinate(String rejectedBy,Long requestId) {
        // fetch the request from db:
        LeaveRequest leaveRequest =leaveRequestRepository.findById(requestId).orElseThrow();

        // set the request:
        leaveRequest.setApprovedByManager(false);
        // if the request is rejected by manager, it is considered as final rejection
        leaveRequest.setStatus(LeaveRequestStatus.REJECTED);

        // save the request:
        leaveRequestRepository.save(leaveRequest);
        // notify the employee:
    }

    @Override
    public void rejectLeave(Long requestId) {
        // Fetch the request:
        LeaveRequest leaveRequest =
                leaveRequestRepository.findById(requestId).orElseThrow();

        // set the leave request:
        leaveRequest.setApprovedByManager(false);
        leaveRequest.setApprovedByManager(false);
        leaveRequest.setStatus(LeaveRequestStatus.REJECTED);

        // save the leave request:
        leaveRequestRepository.save(leaveRequest);
    }
}
