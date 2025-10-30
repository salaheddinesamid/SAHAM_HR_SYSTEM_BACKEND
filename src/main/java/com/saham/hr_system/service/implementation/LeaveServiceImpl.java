package com.saham.hr_system.service.implementation;

import com.saham.hr_system.dto.LeaveRequestDto;
import com.saham.hr_system.exception.InsufficientBalanceException;
import com.saham.hr_system.model.Employee;
import com.saham.hr_system.model.EmployeeBalance;
import com.saham.hr_system.model.LeaveRequest;
import com.saham.hr_system.repository.EmployeeBalanceRepository;
import com.saham.hr_system.repository.EmployeeRepository;
import com.saham.hr_system.repository.LeaveRequestRepository;
import com.saham.hr_system.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                employeeRepository.findByEmail(email).orElseThrow();

        // Fetch employee's balance:
        EmployeeBalance balance = employee.getBalance();

        if(balance.getDaysLeft() == 0){
            throw new InsufficientBalanceException();
        }

        // Otherwise:
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setStartDate(leaveRequestDto.getStartDate());
        leaveRequest.setEndDate(leaveRequestDto.getEndDate());
        leaveRequest.setEmployee(employee);
        leaveRequest.setTypeOfLeave(leaveRequestDto.getType());


        // save the request:
        leaveRequestRepository.save(leaveRequest);
        // reduce the balance:
        balance.setDaysLeft(balance.getDaysLeft() - 1);
        employeeBalanceRepository.save(balance);
    }
}
