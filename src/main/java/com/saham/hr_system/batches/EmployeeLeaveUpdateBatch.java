package com.saham.hr_system.batches;

import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.model.EmployeeBalance;
import com.saham.hr_system.modules.employees.model.LeaveBalanceAdjustment;
import com.saham.hr_system.modules.employees.repository.EmployeeBalanceRepository;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.modules.employees.repository.LeaveBalanceAdjustmentRepository;
import com.saham.hr_system.modules.holidays.dto.HolidayUpdatedEvent;
import com.saham.hr_system.modules.leave.model.Leave;
import com.saham.hr_system.modules.leave.repository.LeaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 */
@Component
public class EmployeeLeaveUpdateBatch {

    private final LeaveRepository leaveRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeBalanceRepository employeeBalanceRepository;
    private final LeaveBalanceAdjustmentRepository leaveBalanceAdjustmentRepository;

    @Autowired
    public EmployeeLeaveUpdateBatch(LeaveRepository leaveRepository, EmployeeRepository employeeRepository, EmployeeBalanceRepository employeeBalanceRepository, LeaveBalanceAdjustmentRepository leaveBalanceAdjustmentRepository) {
        this.leaveRepository = leaveRepository;
        this.employeeRepository = employeeRepository;
        this.employeeBalanceRepository = employeeBalanceRepository;
        this.leaveBalanceAdjustmentRepository = leaveBalanceAdjustmentRepository;
    }

    /**
     * This function takes a list of leaves that are affected by a holiday update, update the total leave days, and the employee balance.
     * Fetch all the leaves that include the start date of the holiday.
     * Replaces the total leave days.
     * Inc/Dec the employee balance affected.
     * @param event: the holiday updated event
     */
    @EventListener(HolidayUpdatedEvent.class)
    public void updateEmployeesLeaveAndBalance(HolidayUpdatedEvent event){
        // Fetch the overlapping leaves:
        List<Leave> overlappingLeaves =
                leaveRepository.findOverlappingLeaves(
                        event.getHoliday().getStartDate(),
                        event.getHoliday().getEndDate()
                );
        // fetch employees
        Set<Employee> employees =
                overlappingLeaves.stream().map(Leave::getEmployee)
                        .collect(Collectors.toSet());
        // fetch employee balances:
        Set<EmployeeBalance> employeeBalances =
                employees.stream().map(Employee::getEmployeeBalance)
                        .collect(Collectors.toSet());

        // recalculate and update the affected leaves
        updateLeaves(overlappingLeaves);
        updateBalances(employeeBalances);
    };

    /**
     * Update the total leave days
     */
    private void updateLeaves(List<Leave> leaves){
        leaves
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
    private void updateBalances(Set<EmployeeBalance> employeeBalances){
        employeeBalances
                . forEach(employeeBalance ->{
                    employeeBalance.setUsedBalance(
                            employeeBalance.getUsedBalance() - 1
                    );
                    LeaveBalanceAdjustment leaveBalanceAdjustment = new LeaveBalanceAdjustment();
                    leaveBalanceAdjustment.setEmployee(employeeBalance.getEmployee());
                    //leaveBalanceAdjustment.setLeave();
                    employeeBalanceRepository.save(employeeBalance);
                });
    }

    private void createLeaveBalanceAdjustment(Employee employee){

    }
}
