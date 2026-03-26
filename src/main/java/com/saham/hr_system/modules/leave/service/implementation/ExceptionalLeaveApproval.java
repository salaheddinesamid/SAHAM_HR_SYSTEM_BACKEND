package com.saham.hr_system.modules.leave.service.implementation;

import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.repository.EmployeeBalanceRepository;
import com.saham.hr_system.modules.leave.model.Leave;
import com.saham.hr_system.modules.leave.model.LeaveRequest;
import com.saham.hr_system.modules.leave.model.LeaveRequestStatus;
import com.saham.hr_system.modules.leave.model.LeaveType;
import com.saham.hr_system.modules.leave.repository.LeaveRepository;
import com.saham.hr_system.modules.leave.repository.LeaveRequestRepository;
import com.saham.hr_system.modules.leave.service.LeaveApproval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class ExceptionalLeaveApproval implements LeaveApproval {

    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveRepository leaveRepository;
    private final LeaveApprovalEmailSenderImpl leaveApprovalEmailSender;
    private final LeaveRequestApprovalEmailSenderImpl leaveRequestApprovalEmailSender;
    private final LeaveRequestRejectionEmailSenderImpl leaveRequestRejectionEmailSender;
    private final LeaveRejectionEmailSenderImpl leaveRejectionEmailSender;
    private final static Logger log = LoggerFactory.getLogger(ExceptionalLeaveApproval.class);

    @Autowired
    public ExceptionalLeaveApproval(LeaveRequestRepository leaveRequestRepository, LeaveRepository leaveRepository, LeaveApprovalEmailSenderImpl leaveApprovalEmailSender, LeaveRequestApprovalEmailSenderImpl leaveRequestApprovalEmailSender, LeaveRequestRejectionEmailSenderImpl leaveRequestRejectionEmailSender, LeaveRejectionEmailSenderImpl leaveRejectionEmailSender) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.leaveRepository = leaveRepository;
        this.leaveApprovalEmailSender = leaveApprovalEmailSender;
        this.leaveRequestApprovalEmailSender = leaveRequestApprovalEmailSender;
        this.leaveRequestRejectionEmailSender = leaveRequestRejectionEmailSender;
        this.leaveRejectionEmailSender = leaveRejectionEmailSender;
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
        try{
            // Fetch the request:
            LeaveRequest leaveRequest =
                    leaveRequestRepository.findById(requestId).orElseThrow();

            // Get the employee:
            Employee employee  = leaveRequest.getEmployee();
            double totalDays =
                    leaveRequest.getTotalDays();

            // check if the request is already approved:
            if(leaveRequest.getStatus().equals(LeaveRequestStatus.APPROVED)){
                log.warn("Leave request with id: {} is already approved.", requestId);
                throw new IllegalStateException("Leave request is already approved.");
            }

            // check if the request is approved by the manager:
            if(!leaveRequest.isApprovedByManager()){
                log.warn("Leave request with id: {} is not approved by the manager yet.", requestId);
                throw new IllegalStateException("Leave request is not approved by the manager yet.");
            }
            // otherwise:
            leaveRequest.setApprovedByHr(true);
            leaveRequest.setStatus(LeaveRequestStatus.APPROVED);

            // create new leave:
            Leave leave = new Leave();
            leave.setEmployee(employee);
            leave.setLeaveType(LeaveType.EXCEPTIONAL); // the type must be set to EXCEPTIONAL
            leave.setFromDate(leaveRequest.getStartDate());
            leave.setToDate(leaveRequest.getEndDate());
            leave.setTotalDays(totalDays);
            leave.setReferenceNumber(leaveRequest.getReferenceNumber());

            // notify the employee and manager:
            CompletableFuture.runAsync(()->{
                try {
                    leaveApprovalEmailSender.sendHRApprovalEmailToEmployee(leaveRequest);
                    leaveApprovalEmailSender.sendHRApprovalEmailToManager(leaveRequest);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            return leaveRepository.save(leave);
        }catch (RuntimeException exception){
            log.error("Error approving exceptional leave request: {}", exception.getMessage());
            throw exception;
        }
    }

    @Override
    public void approveSubordinate(String approvedBy,LeaveRequest leaveRequest) {
        try{
            // set the request:
            leaveRequest.setApprovedByManager(true);
            // save the request:
            leaveRequestRepository.save(leaveRequest);

            // notify the employee and HR:
            CompletableFuture.runAsync(()->{
                try {
                    leaveRequestApprovalEmailSender.sendSubordinateApprovalEmailToEmployee(leaveRequest);
                    leaveRequestApprovalEmailSender.sendSubordinateApprovalEmailToHR(leaveRequest);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }catch (RuntimeException exception){
            log.error("Error approving exceptional leave request by manager: {}", exception.getMessage());
            throw exception;
        }
    }

    @Override
    public void rejectSubordinate(String rejectedBy,LeaveRequest leaveRequest) {
        // set the request:
        leaveRequest.setApprovedByManager(false);
        // if the request is rejected by manager, it is considered as final rejection
        leaveRequest.setStatus(LeaveRequestStatus.REJECTED);

        // save the request:
        leaveRequestRepository.save(leaveRequest);
        // notify the employee:
        CompletableFuture.runAsync(()->{
            try {
                leaveRequestRejectionEmailSender.sendSubordinateRejectionEmailToEmployee(leaveRequest);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void rejectLeave(LeaveRequest leaveRequest) {
        try{
            // set the leave request:
            leaveRequest.setApprovedByManager(false);
            leaveRequest.setApprovedByManager(false);
            leaveRequest.setStatus(LeaveRequestStatus.REJECTED);

            // save the leave request:
            leaveRequestRepository.save(leaveRequest);

            // notify the employee and manager:
            CompletableFuture.runAsync(()->{
                try {
                    leaveRejectionEmailSender.notifyEmployee(leaveRequest);
                    leaveRejectionEmailSender.notifyManager(leaveRequest);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }catch (RuntimeException exception){
            log.error("Error rejecting exceptional leave request: {}", exception.getMessage());
            throw exception;
        }
    }
}
