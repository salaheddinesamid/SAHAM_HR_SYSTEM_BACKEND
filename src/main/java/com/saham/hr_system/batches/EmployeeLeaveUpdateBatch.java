package com.saham.hr_system.batches;

import com.saham.hr_system.modules.employees.model.EmployeeBalance;
import com.saham.hr_system.modules.employees.repository.EmployeeBalanceRepository;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.modules.leave.model.Leave;
import com.saham.hr_system.modules.leave.repository.LeaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class EmployeeLeaveUpdateBatch {

    private final LeaveRepository leaveRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeBalanceRepository employeeBalanceRepository;

    @Autowired
    public EmployeeLeaveUpdateBatch(LeaveRepository leaveRepository, EmployeeRepository employeeRepository, EmployeeBalanceRepository employeeBalanceRepository) {
        this.leaveRepository = leaveRepository;
        this.employeeRepository = employeeRepository;
        this.employeeBalanceRepository = employeeBalanceRepository;
    }

    /**
     * This function takes a list of leaves that are affected by a holiday update, update the total leave days, and the employee balance.
     * Fetch all the leaves that include the start date of the holiday.
     * Replaces the total leave days.
     * Inc/Dec the employee balance affected.
     * @param startDate: the updated start date of the holiday
     */
    public void updateEmployeesLeaveAndBalance(LocalDate startDate){};

    /**
     * Update the total leave days
     */
    private void updateLeaves(List<Leave> leaves){
        leaves.stream()
                .forEach(leave -> {
                    leave.setTotalDays(
                            leave.getTotalDays() - 1
                    );
                    leaveRepository.save(leave);
                });
    }

    /**
     * Update the employee balance, specifically the total balance used.
     */
    private void updateBalances(List<EmployeeBalance> employeeBalances){
        employeeBalances
                .stream()
                .forEach(employeeBalance ->{
                    employeeBalance.setUsedBalance(
                            employeeBalance.getUsedBalance() - 1
                    );
                    employeeBalanceRepository.save(employeeBalance);
                });
    }
}
