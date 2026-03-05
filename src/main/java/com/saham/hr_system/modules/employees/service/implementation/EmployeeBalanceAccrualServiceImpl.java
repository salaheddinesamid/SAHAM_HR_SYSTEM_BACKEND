package com.saham.hr_system.modules.employees.service.implementation;

import com.saham.hr_system.modules.employees.model.EmployeeBalance;
import com.saham.hr_system.modules.employees.repository.EmployeeBalanceRepository;
import com.saham.hr_system.modules.employees.service.EmployeeBalanceAccrualService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeBalanceAccrualServiceImpl implements EmployeeBalanceAccrualService {

    private final EmployeeBalanceRepository employeeBalanceRepository;
    @Override
    public void processMonthlyAccruals() {
        log.info("Starting monthly accrual process...");
        // Fetch all balances
        var balances = employeeBalanceRepository.findAll();

        for(EmployeeBalance balance : balances){
            double monthlyAccrual = balance.getAnnualBalance() / 12.0;
            balance.setAccumulatedBalance(balance.getAccumulatedBalance() + monthlyAccrual);
            // save the updated balance
            employeeBalanceRepository.save(balance);
        }

    }

    @Override
    public void processYearlyAccruals() {
        log.info("Starting yearly accrual process...");
        // Fetch all balances
        var balances = employeeBalanceRepository.findAll();
        for(EmployeeBalance balance : balances){
            // Get the previous year balance
            double previousYearBalance = balance.getRemainderBalance();
            double monthlyAccrual = balance.getAnnualBalance() / 12.0;
            // Set the new annual balance (this could be based on employee's contract, seniority, etc.)
            balance.setYear(LocalDate.now().getYear());
            balance.setAccumulatedBalance(monthlyAccrual);
            balance.setUsedBalance(0);
            balance.setPreviousYearBalance(previousYearBalance);
        }
    }
}
