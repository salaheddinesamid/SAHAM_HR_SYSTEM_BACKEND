package com.saham.hr_system.service.implementation;

import com.saham.hr_system.dto.LeaveRequestDetailsDto;
import com.saham.hr_system.dto.LeaveRequestDto;
import com.saham.hr_system.dto.LeaveRequestResponse;
import com.saham.hr_system.exception.InsufficientBalanceException;
import com.saham.hr_system.exception.UserNotFoundException;
import com.saham.hr_system.model.Employee;
import com.saham.hr_system.model.EmployeeBalance;
import com.saham.hr_system.model.LeaveRequest;
import com.saham.hr_system.model.LeaveRequestStatus;
import com.saham.hr_system.repository.EmployeeBalanceRepository;
import com.saham.hr_system.repository.EmployeeRepository;
import com.saham.hr_system.repository.LeaveRequestRepository;
import com.saham.hr_system.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveServiceImpl implements LeaveService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeBalanceRepository employeeBalanceRepository;

    @Autowired
    public LeaveServiceImpl(LeaveRequestRepository leaveRequestRepository, EmployeeRepository employeeRepository, EmployeeBalanceRepository employeeBalanceRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.employeeRepository = employeeRepository;
        this.employeeBalanceRepository = employeeBalanceRepository;
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
}
