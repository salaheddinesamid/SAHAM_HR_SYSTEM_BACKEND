package com.saham.hr_system.modules.leave.service.implementation;

import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.model.EmployeeBalance;
import com.saham.hr_system.modules.leave.dto.LeaveRequestDto;
import com.saham.hr_system.modules.leave.dto.LeaveRequestResponse;
import com.saham.hr_system.exception.InsufficientBalanceException;
import com.saham.hr_system.exception.LeaveRequestAlreadyApprovedException;
import com.saham.hr_system.exception.LeaveRequestNotApprovedBySupervisorException;
import com.saham.hr_system.exception.UserNotFoundException;
import com.saham.hr_system.modules.leave.model.Leave;
import com.saham.hr_system.modules.leave.model.LeaveRequest;
import com.saham.hr_system.modules.leave.model.LeaveRequestStatus;
import com.saham.hr_system.modules.employees.repository.EmployeeBalanceRepository;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.modules.leave.model.LeaveType;
import com.saham.hr_system.modules.leave.repository.LeaveRepository;
import com.saham.hr_system.modules.leave.repository.LeaveRequestRepository;
import com.saham.hr_system.modules.leave.service.LeaveService;
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
    @Transactional
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
        leaveRequest.setTypeOfLeave(LeaveType.valueOf(leaveRequestDto.getType()));
        leaveRequest.setTypeDetails(leaveRequestDto.getTypeDetails());
        leaveRequest.setRequestDate(LocalDateTime.now());
        leaveRequest.setApprovedByManager(false);
        leaveRequest.setApprovedByHr(false);
        leaveRequest.setStatus(LeaveRequestStatus.IN_PROCESS);

        // save the request:
        leaveRequestRepository.save(leaveRequest);

        // notify manager:
        notifyManager(null);
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

        // Fetch the subordinates
        List<Employee> subordinates = employeeRepository.findAllByManagerId(manager.getId());

        // Fetch leave requests (IN PROCESS ONLY):
        List<LeaveRequest> requests = subordinates.stream()
                .flatMap(employee -> leaveRequestRepository.findAllByEmployeeAndStatusAndApprovedByManager(employee, LeaveRequestStatus.IN_PROCESS, false).stream())
                .toList();


        return requests.isEmpty() ? List.of() : requests.stream()
                .map(LeaveRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<LeaveRequestResponse> getAllLeaveRequestsForHR() {
        // Fetch all leave requests that are pending and approved by managers:
        List<LeaveRequest> requests =
                leaveRequestRepository.findAllByStatusAndApprovedByManager(
                        LeaveRequestStatus.IN_PROCESS, true
                );

        // return a response dto:
        return
                requests.stream().map(LeaveRequestResponse::new).collect(Collectors.toList());
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


        // Get the employee:
        Employee employee  = leaveRequest.getEmployee();

        // Fetch employee balance:
        EmployeeBalance employeeBalance =
                employeeBalanceRepository.findByEmployee(employee).orElseThrow();

        if(leaveRequest.getTypeOfLeave().equals(LeaveType.ANNUAL)){
            approveAnnualLeave(leaveRequest, employeeBalance);
        }

        if(leaveRequest.getTypeOfLeave().equals(LeaveType.EXCEPTIONAL)){
            approveExceptionalLeave(leaveRequest, employeeBalance);
        }

        // Notify the employee:
        notifyEmployee(employee.getEmail());

    }

    private void approveAnnualLeave(LeaveRequest leaveRequest, EmployeeBalance employeeBalance) {

        leaveRequest.setApprovedByHr(true);
        Leave leave = new Leave();
        leave.setFromDate(leaveRequest.getStartDate());
        leave.setToDate(leaveRequest.getEndDate());
        leave.setEmployee(leaveRequest.getEmployee());

        // Do not Decrease employee balance:
        //employeeBalance.setDaysLeft(employeeBalance.getDaysLeft() - leaveRequest.getTotalDays());
        // save the balance:
        employeeBalanceRepository.save(employeeBalance);

        // save the leave request:
        leaveRequestRepository.save(leaveRequest);

        // save the leave:
        leaveRepository.save(leave);
    }

    private void approveExceptionalLeave(LeaveRequest leaveRequest, EmployeeBalance employeeBalance) {
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
    }

    @Override
    public void rejectSubordinateLeaveRequest(Long leaveRequestId) {
        // Fetch leave request:
        LeaveRequest leaveRequest = leaveRequestRepository
                .findById(leaveRequestId).orElseThrow();

        // Check if the request has already been approved:
        if(leaveRequest.getStatus().equals(LeaveRequestStatus.APPROVED)){
            throw new LeaveRequestAlreadyApprovedException(leaveRequest.getEmployee().getEmail());
        }

        // Otherwise:
        leaveRequest.setApprovedByManager(false);
        // leaveRequest.setStatus(LeaveRequestStatus.REJECTED); (the leave request cannot be rejected unless is rejected by HR and Manager)

        leaveRequestRepository.save(leaveRequest);

        // Notify employee:
        notifyEmployee(leaveRequest.getEmployee().getEmail());
    }

    @Override
    public void rejectLeaveRequest(Long leaveRequestId) {
        // Fetch the request:
        LeaveRequest leaveRequest = leaveRequestRepository
                .findById(leaveRequestId)
                .orElseThrow();

        // Check if the request has already been approved:
        if(leaveRequest.getStatus().equals(LeaveRequestStatus.REJECTED)){
            throw new LeaveRequestAlreadyApprovedException(leaveRequest.getEmployee().getEmail());
        }

        // Otherwise:
        leaveRequest.setStatus(LeaveRequestStatus.REJECTED);
        leaveRequestRepository.save(leaveRequest);

        // Notify the employee:
        notifyEmployee(leaveRequest.getEmployee().getEmail());
    }

    // Notify employee of leave request approval or rejection:
    private void notifyEmployee(String email){

    }

    // Notify the manager of subordinate's leave request or approval/rejection:
    private void notifyManager(String email){

    }
}
