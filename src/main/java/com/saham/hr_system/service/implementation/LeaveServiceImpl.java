package com.saham.hr_system.service.implementation;

import com.saham.hr_system.dto.LeaveRequestDetailsDto;
import com.saham.hr_system.dto.LeaveRequestDto;
import com.saham.hr_system.dto.LeaveRequestResponse;
import com.saham.hr_system.exception.InsufficientBalanceException;
import com.saham.hr_system.exception.LeaveRequestNotApprovedBySupervisorException;
import com.saham.hr_system.exception.UserNotFoundException;
import com.saham.hr_system.model.*;
import com.saham.hr_system.repository.EmployeeBalanceRepository;
import com.saham.hr_system.repository.EmployeeRepository;
import com.saham.hr_system.repository.LeaveRepository;
import com.saham.hr_system.repository.LeaveRequestRepository;
import com.saham.hr_system.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveServiceImpl implements LeaveService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeBalanceRepository employeeBalanceRepository;
    private final LeaveRepository leaveRepository;

    @Autowired
    public LeaveServiceImpl(LeaveRequestRepository leaveRequestRepository, EmployeeRepository employeeRepository, EmployeeBalanceRepository employeeBalanceRepository, LeaveRepository leaveRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.employeeRepository = employeeRepository;
        this.employeeBalanceRepository = employeeBalanceRepository;
        this.leaveRepository = leaveRepository;
    }

    @Override
    public void requestLeave(String email ,LeaveRequestDto leaveRequestDto) {

        // Fetch the employee from db:
        Employee employee =
                employeeRepository.findByEmail(email).orElseThrow(()-> new UserNotFoundException(email));

        // Fetch employee's balance:
        EmployeeBalance balance = employeeBalanceRepository.findByEmployee(employee).orElseThrow();

        double totalDays = leaveRequestDto.getStartDate().until(leaveRequestDto.getEndDate()).getDays() + 1;

        if(balance.getDaysLeft() == 0){
            throw new InsufficientBalanceException();
        }

        // Otherwise:
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setStartDate(leaveRequestDto.getStartDate());
        leaveRequest.setEndDate(leaveRequestDto.getEndDate());
        leaveRequest.setEmployee(employee);
        leaveRequest.setTotalDays(totalDays);
        leaveRequest.setTypeOfLeave(leaveRequestDto.getType());
        leaveRequest.setRequestDate(LocalDateTime.now());
        leaveRequest.setStatus(LeaveRequestStatus.IN_PROCESS);

        // save the request:
        leaveRequestRepository.save(leaveRequest);
    }

    @Override
    public List<LeaveRequestResponse> getAllLeaveRequests(String email) {

        // Fetch the employee from db:
        Employee employee =
                employeeRepository.findByEmail(email).orElseThrow();

        List<LeaveRequest> requests = leaveRequestRepository.findAllByEmployee(employee);

        return requests.stream().map(LeaveRequestResponse::new).collect(Collectors.toList());
    }

    @Override
    public List<LeaveRequestResponse> getAllSubordinatesRequests(String email) {
        // Fetch the manager:
        Employee manager = employeeRepository
                .findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));

        // Fetch the subordinates :
        List<Employee> subordinates = employeeRepository.findAllByManagerId(manager.getId());


        List<LeaveRequest> requests = subordinates.stream()
                .flatMap(employee -> leaveRequestRepository.findAllByEmployee(employee).stream())
                .toList();


        return requests.isEmpty() ? List.of() : requests.stream()
                .map(LeaveRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public void approveSubordinateLeaveRequest(Long leaveRequestId) {
        // Fetch the request:
        LeaveRequest leaveRequest =
                leaveRequestRepository.findById(leaveRequestId)
                        .orElseThrow();

        // approve the request:
        leaveRequest.setApprovedByManager(true);

        // save the request:
        leaveRequestRepository.save(leaveRequest);
    }

    @Override
    @Transactional
    public void approveLeaveRequest(Long leaveRequestId) {
        // Fetch the request:
        LeaveRequest leaveRequest =
                leaveRequestRepository.findById(leaveRequestId).orElseThrow();



        // Check if the request has been approved by manager:
        if(!leaveRequest.isApprovedByManager()){
            throw new LeaveRequestNotApprovedBySupervisorException("Leave request has not been approved by supervisor yet.");
        }

        // Get the employee:
        Employee employee  = leaveRequest.getEmployee();

        // Fetch employee balance:
        EmployeeBalance employeeBalance =
                employeeBalanceRepository.findByEmployee(employee).orElseThrow();

        // Otherwise, approve the request and create new leave:
        leaveRequest.setApprovedByHr(true);
        Leave leave = new Leave();
        leave.setFromDate(leaveRequest.getStartDate());
        leave.setToDate(leaveRequest.getEndDate());
        leave.setEmployee(leaveRequest.getEmployee());

        // Decrease employee balance:
        employeeBalance.setDaysLeft(employeeBalance.getDaysLeft() - leaveRequest.getTotalDays());
        // save the balance:
        employeeBalanceRepository.save(employeeBalance);

        // save the leave request:
        leaveRequestRepository.save(leaveRequest);

        // save the leave:
        leaveRepository.save(leave);

        // Notify the employee:
        notifyEmployee(employee.getEmail());

    }

    private void notifyEmployee(String email){

    }
}
