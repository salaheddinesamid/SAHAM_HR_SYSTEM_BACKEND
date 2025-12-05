package com.saham.hr_system.modules.leave.service.implementation;

import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.modules.leave.model.LeaveRequestStatus;
import com.saham.hr_system.modules.leave.repository.LeaveRequestRepository;
import com.saham.hr_system.modules.leave.service.LeaveRequestQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LeaveRequestQueryImpl implements LeaveRequestQuery {

    private final EmployeeRepository employeeRepository;
    private final LeaveRequestRepository leaveRequestRepository;

    @Autowired
    public LeaveRequestQueryImpl(EmployeeRepository employeeRepository, LeaveRequestRepository leaveRequestRepository) {
        this.employeeRepository = employeeRepository;
        this.leaveRequestRepository = leaveRequestRepository;
    }

    @Override
    public long getNumberOfInProcessSubordinatesLeaveRequests(String managerEmail) {
        // fetch the manager:
        Employee manager = employeeRepository.findByEmail(managerEmail)
                .orElseThrow();

        // fetch subordinates:
        List<Employee> subordinates =
                employeeRepository.findAllByManagerId(manager.getId());

        // count in-process leave requests:
        return
                subordinates.stream()
                        .map(sub-> leaveRequestRepository.countByEmployeeAndStatus(
                                sub,
                                LeaveRequestStatus.IN_PROCESS
                        )).reduce(0L, Long::sum);
    }
}
