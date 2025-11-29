package com.saham.hr_system.modules.leave.service.implementation;

import com.saham.hr_system.exception.InsufficientBalanceException;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.model.EmployeeBalance;
import com.saham.hr_system.modules.employees.repository.EmployeeBalanceRepository;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.modules.leave.dto.LeaveRequestDto;
import com.saham.hr_system.modules.leave.model.LeaveRequest;
import com.saham.hr_system.modules.leave.model.LeaveRequestStatus;
import com.saham.hr_system.modules.leave.model.LeaveType;
import com.saham.hr_system.modules.leave.repository.LeaveRequestRepository;
import com.saham.hr_system.modules.leave.service.LeaveProcessor;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Component
public class ExceptionalLeaveRequestProcessor implements LeaveProcessor {

    private final EmployeeRepository employeeRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeBalanceRepository employeeBalanceRepository;
    private final LeaveRequestEmailSenderImpl leaveRequestEmailSender;
    private final LeaveDocumentStorageServiceImpl leaveDocumentStorageService;

    @Autowired
    public ExceptionalLeaveRequestProcessor(EmployeeRepository employeeRepository, LeaveRequestRepository leaveRequestRepository, EmployeeBalanceRepository employeeBalanceRepository, LeaveRequestEmailSenderImpl leaveRequestEmailSender, LeaveDocumentStorageServiceImpl leaveDocumentStorageService) {
        this.employeeRepository = employeeRepository;
        this.leaveRequestRepository = leaveRequestRepository;
        this.employeeBalanceRepository = employeeBalanceRepository;
        this.leaveRequestEmailSender = leaveRequestEmailSender;
        this.leaveDocumentStorageService = leaveDocumentStorageService;
    }

    @Override
    public boolean supports(String leaveType) {
        return LeaveType.EXCEPTIONAL.equals(LeaveType.valueOf(leaveType));
    }

    @Override
    public LeaveRequest process(String email, LeaveRequestDto requestDto, MultipartFile file) throws IOException, MessagingException {
        // fetch the employee from db:
        Employee employee =
                employeeRepository.findByEmail(email).orElseThrow();
        // fetch the balance
        EmployeeBalance balance = employeeBalanceRepository
                .findByEmployee(employee).orElseThrow();

        double totalDays = requestDto.getStartDate().until(requestDto.getEndDate()).getDays() + 1;

        if(balance.getDaysLeft() == 0){
            throw new InsufficientBalanceException();
        }

        // Otherwise:
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setStartDate(requestDto.getStartDate());
        leaveRequest.setEndDate(requestDto.getEndDate());
        leaveRequest.setEmployee(employee);
        leaveRequest.setTotalDays(totalDays);
        leaveRequest.setTypeOfLeave(LeaveType.valueOf(requestDto.getType()));
        leaveRequest.setTypeDetails(requestDto.getTypeDetails());
        leaveRequest.setRequestDate(LocalDateTime.now());
        leaveRequest.setComment(requestDto.getComment());
        leaveRequest.setApprovedByManager(false);
        leaveRequest.setApprovedByHr(false);
        leaveRequest.setStatus(LeaveRequestStatus.IN_PROCESS);

        // notify the employee:
        CompletableFuture.runAsync(()->{
                    try {
                        leaveRequestEmailSender.sendEmployeeNotificationEmail(leaveRequest);
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        // notify the manager:
        CompletableFuture.runAsync(()->{
                    try {
                        leaveRequestEmailSender.sendManagerNotificationEmail(leaveRequest);
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        // save the request:
        return leaveRequestRepository.save(leaveRequest);
    }

}
