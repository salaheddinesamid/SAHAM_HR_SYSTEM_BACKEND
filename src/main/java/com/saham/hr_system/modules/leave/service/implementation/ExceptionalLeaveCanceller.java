package com.saham.hr_system.modules.leave.service.implementation;

import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.leave.model.Leave;
import com.saham.hr_system.modules.leave.model.LeaveRequest;
import com.saham.hr_system.modules.leave.model.LeaveRequestStatus;
import com.saham.hr_system.modules.leave.model.LeaveType;
import com.saham.hr_system.modules.leave.repository.LeaveRepository;
import com.saham.hr_system.modules.leave.repository.LeaveRequestRepository;
import com.saham.hr_system.modules.leave.service.LeaveCanceller;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class ExceptionalLeaveCanceller implements LeaveCanceller {
    private final LeaveRepository leaveRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveCancellerEmailSenderImpl leaveCancellerEmailSender;

    @Autowired
    public ExceptionalLeaveCanceller(LeaveRepository leaveRepository, LeaveRequestRepository leaveRequestRepository, LeaveCancellerEmailSenderImpl leaveCancellerEmailSender) {
        this.leaveRepository = leaveRepository;
        this.leaveRequestRepository = leaveRequestRepository;
        this.leaveCancellerEmailSender = leaveCancellerEmailSender;
    }

    @Override
    public boolean supports(String status) {
        return LeaveType.EXCEPTIONAL.equals(LeaveType.valueOf(status));
    }

    @Override
    public void cancel(String refNumber) {
        // fetch the leave from the db:
        Leave leave = leaveRepository.findByReferenceNumber(refNumber)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        // fetch the request from db:
        LeaveRequest leaveRequest = leaveRequestRepository
                .findByReferenceNumber(refNumber).orElseThrow(() -> new RuntimeException("Leave request not found"));

        // get the employee:
        Employee employee = leave.getEmployee();

        // set the request to cancel:
        leaveRequest.setStatus(LeaveRequestStatus.CANCELED);

        // remove employee's leave:
        employee.getLeaves().remove(leave);

        // remove the leave from db:
        leaveRepository.delete(leave);

        // save the request in db:
        leaveRequestRepository.save(leaveRequest);

        // notify the manager and the employee by email asynchronously
        CompletableFuture.runAsync(()->{
            try{
                leaveCancellerEmailSender.notifyManager(leave);
                leaveCancellerEmailSender.notifyEmployee(leave);
            }catch (MessagingException e){
                throw new RuntimeException(e);
            }
        });

    }
}
