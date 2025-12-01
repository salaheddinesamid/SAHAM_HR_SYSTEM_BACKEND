package com.saham.hr_system.modules.leave.service.implementation;

import com.saham.hr_system.exception.LeaveRequestAlreadyApprovedException;
import com.saham.hr_system.exception.LeaveRequestNotApprovedBySupervisorException;
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
import com.saham.hr_system.modules.leave.service.LeaveApprovalEmailSender;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class AnnualLeaveApproval implements LeaveApproval {

    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveRepository leaveRepository;
    private final EmployeeBalanceRepository employeeBalanceRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveApprovalEmailSenderImpl leaveApprovalEmailSender;
    private final LeaveRequestApprovalEmailSenderImpl leaveRequestApprovalEmailSender;
    private final LeaveRequestRejectionEmailSenderImpl leaveRequestRejectionEmailSender;

    @Autowired
    public AnnualLeaveApproval(LeaveRequestRepository leaveRequestRepository,
                               LeaveRepository leaveRepository, EmployeeBalanceRepository employeeBalanceRepository, LeaveApprovalEmailSenderImpl leaveApprovalEmailSender,
                               EmployeeRepository employeeRepository, LeaveRequestApprovalEmailSenderImpl leaveRequestApprovalEmailSender, LeaveRequestRejectionEmailSenderImpl leaveRequestRejectionEmailSender) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.leaveRepository = leaveRepository;
        this.employeeBalanceRepository = employeeBalanceRepository;
        this.employeeRepository = employeeRepository;
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
        // Fetch the request:
        LeaveRequest leaveRequest =
                leaveRequestRepository.findById(requestId).orElseThrow();

        // Get the employee:
        Employee employee  = leaveRequest.getEmployee();

        // Fetch employee balance:
        EmployeeBalance employeeBalance =
                employeeBalanceRepository.findByEmployee(employee).orElseThrow();

        // Check if the request is approved by manager:
        if(!leaveRequest.isApprovedByManager()){
            throw new LeaveRequestNotApprovedBySupervisorException(leaveRequest.getLeaveRequestId().toString());
        }
        // Check if the request has already been approved:
        if(leaveRequest.getStatus().equals(LeaveRequestStatus.APPROVED)){
            throw new LeaveRequestAlreadyApprovedException(leaveRequest.getEmployee().getEmail());
        }
        // Check if the request has already been declined:
        if(leaveRequest.getStatus().equals(LeaveRequestStatus.REJECTED)){
            throw new LeaveRequestAlreadyApprovedException(leaveRequest.getEmployee().getEmail());
        }
        // Otherwise:

        double totalDays =
                leaveRequest.getTotalDays();
        log.info("Total days requested: {}", totalDays);
        log.info("Employee balance days left: {}", employeeBalance.getDaysLeft());

        // subtract total days from the balance
        employeeBalance.setDaysLeft(employeeBalance.getDaysLeft() - totalDays);
        employeeBalance.setUsedBalance(employeeBalance.getUsedBalance() + totalDays);
        log.info("Employee balance days left after deduction: {}", employeeBalance.getDaysLeft());

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

        // notify the employee:
        CompletableFuture.runAsync(()->{
            try {
                leaveApprovalEmailSender.sendHRApprovalEmailToEmployee(leaveRequest);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        });
        // notify the Manager:
        CompletableFuture.runAsync(()->{
            try {
                leaveApprovalEmailSender.sendHRApprovalEmailToManager(leaveRequest);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        });
        // save the leave in the database
        return leaveRepository.save(leave);
    }

    @Override
    public void approveSubordinate(String approvedBy,Long requestId) {
        // Fetch the request:
        LeaveRequest leaveRequest =
                leaveRequestRepository.findById(requestId)
                        .orElseThrow();
        // Fetch the employee:
        Employee employee  = leaveRequest.getEmployee();
        // Fetch the manager:
        Employee manager = employee.getManager();

        if(!manager.getEmail().equals(approvedBy)){
            throw new SecurityException("You are not authorized to approve this request");
        }

        // approve the request:
        leaveRequest.setApprovedByManager(true);

        // save the request:
        leaveRequestRepository.save(leaveRequest);

        // notify the employee
        CompletableFuture.runAsync(()->{
            try {
                leaveRequestApprovalEmailSender.sendSubordinateApprovalEmailToEmployee(leaveRequest);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        });
        // notify the HR:
        CompletableFuture.runAsync(()->{
            try {
                leaveRequestApprovalEmailSender.sendSubordinateApprovalEmailToHR(leaveRequest);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void rejectSubordinate(String rejectedBy,Long requestId) {
        // Fetch leave request:
        LeaveRequest leaveRequest = leaveRequestRepository
                .findById(requestId).orElseThrow();

        // Fetch the employee:
        Employee employee  = leaveRequest.getEmployee();
        // Fetch the manager:
        Employee manager = employee.getManager();

        if(!manager.getEmail().equals(rejectedBy)){
            throw new SecurityException("You are not authorized to approve this request");
        }

        // Check if the request has already been approved:
        if(leaveRequest.getStatus().equals(LeaveRequestStatus.APPROVED)){
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

    }

    @Override
    public void rejectLeave(Long requestId) {
        // Fetch the request:
        LeaveRequest leaveRequest = leaveRequestRepository
                .findById(requestId)
                .orElseThrow();

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
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        });
        // notify the manager:
        CompletableFuture.runAsync(()->{
            try {
                leaveRequestRejectionEmailSender.sendSubordinateRejectionEmailToEmployee(leaveRequest);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
