package com.saham.hr_system.modules.leave.service.implementation;

import com.saham.hr_system.exception.UserNotFoundException;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.repository.EmployeeBalanceRepository;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.modules.leave.dto.LeaveRequestDto;
import com.saham.hr_system.modules.leave.exception.InvalidDatesException;
import com.saham.hr_system.modules.leave.exception.MissingFieldException;
import com.saham.hr_system.modules.leave.model.LeaveRequest;
import com.saham.hr_system.modules.leave.model.LeaveRequestStatus;
import com.saham.hr_system.modules.leave.model.LeaveType;
import com.saham.hr_system.modules.leave.repository.LeaveRequestRepository;
import com.saham.hr_system.modules.leave.service.LeaveProcessor;
import com.saham.hr_system.modules.leave.utils.LeaveRequestRefNumberGenerator;
import com.saham.hr_system.utils.TotalDaysCalculator;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Component
public class DefaultLeaveRequestProcessor implements LeaveProcessor {

    private final EmployeeRepository employeeRepository;
    private final EmployeeBalanceRepository employeeBalanceRepository;
    private final LeaveRequestEmailSenderImpl leaveRequestEmailSender;
    private final LeaveRequestRepository leaveRequestRepository;
    private final TotalDaysCalculator leaveDaysCalculator;
    private final LeaveRequestRefNumberGenerator leaveRequestRefNumberGenerator;

    private final static Logger log = LoggerFactory.getLogger(DefaultLeaveRequestProcessor.class);

    @Autowired
    public DefaultLeaveRequestProcessor(EmployeeRepository employeeRepository, EmployeeBalanceRepository employeeBalanceRepository, LeaveRequestEmailSenderImpl leaveRequestEmailSender, LeaveRequestRepository leaveRequestRepository, TotalDaysCalculator leaveDaysCalculator, LeaveRequestRefNumberGenerator leaveRequestRefNumberGenerator) {
        this.employeeRepository = employeeRepository;
        this.employeeBalanceRepository = employeeBalanceRepository;
        this.leaveRequestEmailSender = leaveRequestEmailSender;
        this.leaveRequestRepository = leaveRequestRepository;
        this.leaveDaysCalculator = leaveDaysCalculator;
        this.leaveRequestRefNumberGenerator = leaveRequestRefNumberGenerator;
    }

    @Override
    public boolean supports(String leaveType) {
        return LeaveType.ANNUAL.equals(LeaveType.valueOf(leaveType));
    }

    @Override
    public LeaveRequest process(String email, LeaveRequestDto requestDto) throws MessagingException, InvalidDatesException, MissingFieldException {
        try{
            // fetch the employee from db:
            Employee employee =
                    employeeRepository.findByEmail(email).orElseThrow(()-> {
                        log.warn("Employee with email: {} not found", email);
                        return new UserNotFoundException("Employee with email: " + email + " not found");
                    });

            // calculate the total days excluding the weekends and holidays
            double totalDays =
                    leaveDaysCalculator.calculateTotalDays(requestDto.getStartDate(), requestDto.getEndDate());

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

            String refNumber = leaveRequestRefNumberGenerator.generate(leaveRequest);
            // set the leave request Ref Number:
            leaveRequest.setReferenceNumber(refNumber);
            LeaveRequest savedRequest = leaveRequestRepository.save(leaveRequest); // save the leave request before sending the emails

            // notify the manager:
            CompletableFuture.runAsync(() ->
                    {
                        try {
                            leaveRequestEmailSender.sendEmployeeNotificationEmail(leaveRequest);
                            leaveRequestEmailSender.sendManagerNotificationEmail(leaveRequest);
                        } catch (MessagingException e) {
                            throw new RuntimeException(e);
                        }
                    }
            );
            // return the saved request:
            return savedRequest;
        }catch (RuntimeException ex){
            log.error("Error processing leave request for employee with email: {}, error message: {}", email, ex.getMessage());
            throw ex;
        }
    }
}
