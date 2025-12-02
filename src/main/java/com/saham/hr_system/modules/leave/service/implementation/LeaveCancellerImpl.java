package com.saham.hr_system.modules.leave.service.implementation;

import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.model.EmployeeBalance;
import com.saham.hr_system.modules.employees.repository.EmployeeBalanceRepository;
import com.saham.hr_system.modules.leave.model.Leave;
import com.saham.hr_system.modules.leave.model.LeaveRequest;
import com.saham.hr_system.modules.leave.model.LeaveRequestStatus;
import com.saham.hr_system.modules.leave.repository.LeaveRepository;
import com.saham.hr_system.modules.leave.repository.LeaveRequestRepository;
import com.saham.hr_system.modules.leave.service.LeaveRequestCanceller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class LeaveCancellerImpl implements LeaveRequestCanceller {

    private final LeaveRepository leaveRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeBalanceRepository employeeBalanceRepository;

    @Autowired
    public LeaveCancellerImpl(LeaveRepository leaveRepository, LeaveRequestRepository leaveRequestRepository, EmployeeBalanceRepository employeeBalanceRepository) {
        this.leaveRepository = leaveRepository;
        this.leaveRequestRepository = leaveRequestRepository;
        this.employeeBalanceRepository = employeeBalanceRepository;
    }

    @Override
    public boolean supports(String status) {
        return LeaveRequestStatus.APPROVED.equals(LeaveRequestStatus.valueOf(status));
    }

    /**
     * This function implements the cancellation of an approved leave request
     * @param refNumber
     */
    @Override
    @Transactional
    public void cancel(String refNumber) {
        // fetch the leave from db:
        Leave leave = leaveRepository.findByReferenceNumber(refNumber).orElseThrow();

        // fetch the request:
        LeaveRequest leaveRequest = leaveRequestRepository.findByReferenceNumber(refNumber)
                .orElseThrow();

        // fetch the employee balance and update it
        Employee employee = leave.getEmployee();
        EmployeeBalance employeeBalance = employeeBalanceRepository.findByEmployee(employee).orElseThrow();

        log.info("Employee total balance before cancellation is: {}", employeeBalance.getDaysLeft());
        // get the total days of the leave:
        double totalDays = leave.getTotalDays();
        log.info("Leave cancelled by " + employee.getFullName());

        // remove the leave from employee leaves:
        employee.getLeaves().remove(leave);

        // update the leave request:
        leaveRequest.setStatus(LeaveRequestStatus.CANCELED);

        // update the balance:
        employeeBalance.setDaysLeft(employeeBalance.getDaysLeft() + totalDays);
        employeeBalance.setUsedBalance(employeeBalance.getUsedBalance() - totalDays);

        log.info("Employee total balance after cancellation is: {}", employeeBalance.getDaysLeft());

        // delete the leave:
        leaveRepository.delete(leave);
        // save the balance:
        employeeBalanceRepository.save(employeeBalance);
    }
}
