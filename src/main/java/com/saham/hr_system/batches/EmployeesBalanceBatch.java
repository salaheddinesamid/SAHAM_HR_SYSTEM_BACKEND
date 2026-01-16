package com.saham.hr_system.batches;

import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.model.EmployeeBalance;
import com.saham.hr_system.modules.employees.repository.EmployeeBalanceRepository;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 */
@Component
@RequiredArgsConstructor
public class EmployeesBalanceBatch {
    private final EmployeeRepository employeeRepository;
    private final EmployeeBalanceRepository employeeBalanceRepository;

    @Scheduled(cron = "0 0 0 1 * ?")
    private void updateEmployeesBalances(){
        List<Employee> employees = employeeRepository
                .findAll();
        employees.stream()
                .forEach(employee -> {
                    EmployeeBalance balance = employee.getEmployeeBalance();
                    balance.setAnnualBalance(balance.getAnnualBalance());
                    balance.setUsedBalance(0);
                    balance.setAnnualBalance(0);
                });
    }
}
