package com.saham.hr_system.modules.leave.service.implementation;

import com.saham.hr_system.exception.LeaveRequestAlreadyApprovedException;
import com.saham.hr_system.exception.LeaveRequestNotApprovedBySupervisorException;
import com.saham.hr_system.exception.UnauthorizedAccessException;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.model.EmployeeBalance;
import com.saham.hr_system.modules.employees.repository.EmployeeBalanceRepository;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.modules.leave.model.Leave;
import com.saham.hr_system.modules.leave.model.LeaveRequest;
import com.saham.hr_system.modules.leave.model.LeaveRequestStatus;
import com.saham.hr_system.modules.leave.model.LeaveType;
import com.saham.hr_system.modules.leave.repository.LeaveRepository;
import com.saham.hr_system.modules.leave.repository.LeaveRequestRepository;
import com.saham.hr_system.modules.leave.service.LeaveApproval;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class AnnualLeaveApproval implements LeaveApproval {

    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveRepository leaveRepository;
    private final EmployeeBalanceRepository employeeBalanceRepository;
    private final LeaveApprovalEmailSenderImpl leaveApprovalEmailSender;
    private final LeaveRequestApprovalEmailSenderImpl leaveRequestApprovalEmailSender;
    private final LeaveRequestRejectionEmailSenderImpl leaveRequestRejectionEmailSender;

    private final static Logger log = LoggerFactory.getLogger(AnnualLeaveApproval.class);

    @Autowired
    public AnnualLeaveApproval(LeaveRequestRepository leaveRequestRepository,
                               LeaveRepository leaveRepository, EmployeeBalanceRepository employeeBalanceRepository, LeaveApprovalEmailSenderImpl leaveApprovalEmailSender,
                               LeaveRequestApprovalEmailSenderImpl leaveRequestApprovalEmailSender, LeaveRequestRejectionEmailSenderImpl leaveRequestRejectionEmailSender) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.leaveRepository = leaveRepository;
        this.employeeBalanceRepository = employeeBalanceRepository;
        this.leaveRequestApprovalEmailSender = leaveRequestApprovalEmailSender;
        this.leaveRequestRejectionEmailSender = leaveRequestRejectionEmailSender;
        this.leaveApprovalEmailSender = leaveApprovalEmailSender;
    }

    @Override
    public boolean supports(String leaveType) {
        return LeaveType.ANNUAL.equals(LeaveType.valueOf(leaveType));
    }

    @Override
    public Leave approve(Long requestId) {
        try{
            // Fetch the request:
            LeaveRequest leaveRequest =
                    leaveRequestRepository.findById(requestId).orElseThrow();

            // Get the employee:
            Employee employee  = leaveRequest.getEmployee();

            // Fetch employee balance:
            EmployeeBalance employeeBalance =
                    employee.getEmployeeBalance();

            // Check if the request is approved by manager:
            if(!leaveRequest.isApprovedByManager()){
                log.warn("Leave request with id: {} is not approved by the supervisor", leaveRequest.getLeaveRequestId());
                throw new LeaveRequestNotApprovedBySupervisorException(leaveRequest.getLeaveRequestId().toString());
            }
            // Check if the request has already been approved:
            if(leaveRequest.getStatus().equals(LeaveRequestStatus.APPROVED)){
                log.warn("Leave request with id: {} has already been approved", leaveRequest.getLeaveRequestId());
                throw new LeaveRequestAlreadyApprovedException(leaveRequest.getEmployee().getEmail());
            }
            // Check if the request has already been rejected:
            if(leaveRequest.getStatus().equals(LeaveRequestStatus.REJECTED)){
                log.warn("Leave request with id: {} has already been rejected", leaveRequest.getLeaveRequestId());
                throw new LeaveRequestAlreadyApprovedException(leaveRequest.getEmployee().getEmail());
            }
            // Otherwise:

            double totalDays =
                    leaveRequest.getTotalDays();
            log.info("Total days requested: {}", totalDays);
            log.info("Employee current balance: {}", employeeBalance.getRemainderBalance()); // log the current balance

            // update the balance:
            employeeBalance.setUsedBalance(employeeBalance.getUsedBalance() + totalDays);
            log.info("Employee balance days left after deduction: {}", employeeBalance.getRemainderBalance()); // log the post current balance

            // save the balance:
            employeeBalanceRepository.save(employeeBalance);

            // update the leave request:
            leaveRequest.setStatus(LeaveRequestStatus.APPROVED);
            leaveRequestRepository.save(leaveRequest);

            // create new leave:
            Leave leave = new Leave();
            leave.setEmployee(employee);
            leave.setLeaveType(LeaveType.ANNUAL);
            leave.setFromDate(leaveRequest.getStartDate());
            leave.setToDate(leaveRequest.getEndDate());
            leave.setTotalDays(totalDays);
            leave.setReferenceNumber(leaveRequest.getReferenceNumber());

            // notify the employee:
            CompletableFuture.runAsync(()->{
                try {
                    leaveApprovalEmailSender.sendHRApprovalEmailToEmployee(leaveRequest);
                    leaveApprovalEmailSender.sendHRApprovalEmailToManager(leaveRequest);

                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            });
            // save the leave in the database
            return leaveRepository.save(leave);
        }catch (RuntimeException exception){
            log.error("Error approving leave request: {}", exception.getMessage());
            throw exception;
        }
    }

    @Override
    public void approveSubordinate(String approvedBy,LeaveRequest leaveRequest) {
        try{
            // Fetch the employee:
            Employee employee  = leaveRequest.getEmployee();
            // Fetch the manager:
            Employee manager = employee.getManager() == null ? employee.getEmployeeProfessionalDetails().getManager() : employee.getManager();

            assert manager != null;
            if(!manager.getEmail().equals(approvedBy)){
                throw new UnauthorizedAccessException("You are not authorized to approve this request");
            }

            // approve the request:
            leaveRequest.setApprovedByManager(true);

            // save the request:
            leaveRequestRepository.save(leaveRequest);

            // notify the employee and HR
            CompletableFuture.runAsync(()->{
                try {
                    leaveRequestApprovalEmailSender.sendSubordinateApprovalEmailToEmployee(leaveRequest);
                    leaveRequestApprovalEmailSender.sendSubordinateApprovalEmailToHR(leaveRequest);
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            });
        }catch (RuntimeException exception){
            log.error("Error approving leave request by supervisor: {}", exception.getMessage());
            throw exception;
        }
    }

    @Override
    public void rejectSubordinate(String rejectedBy,LeaveRequest leaveRequest) {
        try{
            // Fetch the employee:
            Employee employee  = leaveRequest.getEmployee();
            // Fetch the manager:
            Employee manager = employee.getManager() == null ? employee.getEmployeeProfessionalDetails().getManager() : employee.getManager();
            assert manager != null;

            if(!manager.getEmail().equals(rejectedBy)){
                log.warn("Unauthorized attempt to reject leave request with id: {} by user: {}", leaveRequest.getLeaveRequestId(), rejectedBy);
                throw new UnauthorizedAccessException("You are not authorized to reject this request");
            }

            // Check if the request has already been approved:
            if(leaveRequest.getStatus().equals(LeaveRequestStatus.APPROVED)){
                log.warn("Attempt to reject already approved leave request with id: {} by user: {}", leaveRequest.getLeaveRequestId(), rejectedBy);
                throw new LeaveRequestAlreadyApprovedException(leaveRequest.getEmployee().getEmail());
            }

            // Otherwise:
            leaveRequest.setApprovedByManager(false);
            leaveRequest.setStatus(LeaveRequestStatus.REJECTED);

            leaveRequestRepository.save(leaveRequest);

            // notify the employee:
            CompletableFuture.runAsync(()->{
                try {
                    leaveRequestRejectionEmailSender.sendSubordinateRejectionEmailToEmployee(leaveRequest);
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            });

        }catch (RuntimeException exception){
            log.error("Error rejecting leave request by supervisor: {}", exception.getMessage());
            throw exception;
        }
    }

    @Override
    public void rejectLeave(LeaveRequest leaveRequest) {
        try{
            // Check if the request has already been approved:
            if(leaveRequest.getStatus().equals(LeaveRequestStatus.REJECTED)){
                throw new LeaveRequestAlreadyApprovedException(leaveRequest.getEmployee().getEmail());
            }

            // Otherwise:
            leaveRequest.setStatus(LeaveRequestStatus.REJECTED);
            leaveRequestRepository.save(leaveRequest);

            // notify the employee:
            CompletableFuture.runAsync(()->{
                try {
                    leaveRequestRejectionEmailSender.sendSubordinateRejectionEmailToEmployee(leaveRequest);
                    leaveRequestRejectionEmailSender.sendSubordinateRejectionEmailToEmployee(leaveRequest);
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }

            });
        }catch (RuntimeException exception){
            log.error("Error rejecting leave request: {}", exception.getMessage());
            throw exception;
        }
    }
}
