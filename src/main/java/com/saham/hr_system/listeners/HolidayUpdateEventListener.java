package com.saham.hr_system.listeners;

import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.model.EmployeeBalance;
import com.saham.hr_system.modules.employees.model.LeaveBalanceAdjustment;
import com.saham.hr_system.modules.employees.repository.EmployeeBalanceRepository;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.modules.employees.repository.LeaveBalanceAdjustmentRepository;
import com.saham.hr_system.modules.holidays.dto.HolidayUpdatedEvent;
import com.saham.hr_system.modules.leave.model.Leave;
import com.saham.hr_system.modules.leave.repository.LeaveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/** * This class listens to the HolidayUpdatedEvent and updates the affected employee leaves and balances accordingly.
 * When a holiday is updated, it fetches all the leaves that overlap with the holiday dates, recalculates the total leave days for those leaves, and updates the employee balances accordingly.
 */
@Component
@Slf4j
public class HolidayUpdateEventListener {

    private final LeaveRepository leaveRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeBalanceRepository employeeBalanceRepository;
    private final LeaveBalanceAdjustmentRepository leaveBalanceAdjustmentRepository;

    @Autowired
    public HolidayUpdateEventListener(LeaveRepository leaveRepository, EmployeeRepository employeeRepository, EmployeeBalanceRepository employeeBalanceRepository, LeaveBalanceAdjustmentRepository leaveBalanceAdjustmentRepository) {
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
    @Transactional
    public void updateEmployeesLeaveAndBalance(HolidayUpdatedEvent event){
        log.info("Received HolidayUpdatedEvent for holiday Name: {}", event.getHoliday().getName());
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
        // recalculate the total leave days
        log.info("Recalculating leaves and balances for {} affected leaves with start date: {}", overlappingLeaves.size(), event.getHoliday().getStartDate());

        long totalLeaveDays = 0;
        // recalculate and update the affected leaves
        updateEmployeeLeaveAndBalance(
                overlappingLeaves,
                employeeBalances
        );
    };

    /**
     * Update the affected employee leaves and balances
     * @param leaves: the overlapping leaves
     * @param balances: the concerned employee balances
     */
    @Transactional
    protected void updateEmployeeLeaveAndBalance(
            List<Leave> leaves, Set<EmployeeBalance> balances
    ){
        log.info("Updating {} leaves and {} employee balances.", leaves.size(), balances.size());
        Set<Leave> updatedLeaves = leaves
                .stream()
                .map(leave -> {
                    // Create new leave adjustment record to track the leave and balance changes
                    LeaveBalanceAdjustment leaveBalanceAdjustment = new LeaveBalanceAdjustment();
                    // Update the total leave days
                    leave.setTotalDays(
                            leave.getTotalDays() - 1
                    );
                    Leave updatedLeave = leaveRepository.save(leave);
                    leaveBalanceAdjustment.setLeave(updatedLeave);
                    leaveBalanceAdjustment.setDelta(1);
                    leaveBalanceAdjustment.setReason("Holiday update adjustment");
                    leaveBalanceAdjustment.setEmployee(updatedLeave.getEmployee());
                    leaveBalanceAdjustmentRepository.save(leaveBalanceAdjustment);
                    return updatedLeave;
                }).collect(Collectors.toSet());
        Set<EmployeeBalance> updatedBalances  = balances
                .stream()
                .map(employeeBalance -> {
                    // Update the used balance
                    employeeBalance.setUsedBalance(
                            employeeBalance.getUsedBalance() + 1
                    );
                    return employeeBalanceRepository.save(employeeBalance);
                }).collect(Collectors.toSet());
    }
}
