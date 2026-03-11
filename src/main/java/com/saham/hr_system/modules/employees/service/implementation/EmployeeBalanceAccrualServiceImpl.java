package com.saham.hr_system.modules.employees.service.implementation;

import com.saham.hr_system.modules.employees.model.BalanceAccrualHistory;
import com.saham.hr_system.modules.employees.model.BalanceAccrualType;
import com.saham.hr_system.modules.employees.model.EmployeeBalance;
import com.saham.hr_system.modules.employees.repository.BalanceAccrualRepository;
import com.saham.hr_system.modules.employees.repository.EmployeeBalanceRepository;
import com.saham.hr_system.modules.employees.service.EmployeeBalanceAccrualService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeBalanceAccrualServiceImpl implements EmployeeBalanceAccrualService {

    private final EmployeeBalanceRepository employeeBalanceRepository;
    private final BalanceAccrualRepository balanceAccrualRepository;

    @Override
    @Transactional
    public void processMonthlyAccruals() {
        log.info("Starting monthly accrual process...");
        // Check if the current month is january:
        if(LocalDate.now().getMonth().equals(Month.JANUARY)){
            log.info("Monthly accrual process should run on the first day of the month. Skipping...");
            return;
        }
        // Creates a new record for the monthly accrual process:
        BalanceAccrualHistory balanceAccrualHistory = new BalanceAccrualHistory();
        balanceAccrualHistory.setBalanceAccrualType(BalanceAccrualType.MONTHLY);
        balanceAccrualHistory.setYear(LocalDate.now().getYear());
        balanceAccrualHistory.setExecutionDate(LocalDateTime.now());
        // Fetch all balances
        var balances = employeeBalanceRepository.findAllForUpdate();
        double totalOperations = 0;
        for(EmployeeBalance balance : balances){
            double monthlyAccrual = balance.getAnnualBalance() / 12.0;
            balance.setAccumulatedBalance(balance.getAccumulatedBalance() + monthlyAccrual);
            balance.setLastUpdated(LocalDateTime.now());
            // save the updated balance
            employeeBalanceRepository.save(balance);
            totalOperations += 1;
        }
        // set the total operations in the history record and save it:
        balanceAccrualHistory.setTotalOperations(totalOperations);
        balanceAccrualRepository.save(balanceAccrualHistory);
    }

    @Override
    @Transactional
    public void processYearlyAccruals() {
        log.info("Starting yearly accrual process...");
        BalanceAccrualHistory balanceAccrualHistory = new BalanceAccrualHistory();
        balanceAccrualHistory.setBalanceAccrualType(BalanceAccrualType.YEARLY);
        balanceAccrualHistory.setYear(LocalDate.now().getYear());
        balanceAccrualHistory.setExecutionDate(LocalDateTime.now());
        // Fetch all balances
        var balances = employeeBalanceRepository.findAllForUpdate();
        double totalOperations = 0;
        for(EmployeeBalance balance : balances){
            // Get the previous year balance
            double previousYearBalance = balance.getRemainderBalance();
            double monthlyAccrual = balance.getAnnualBalance() / 12.0;
            // Set the new annual balance (this could be based on employee's contract, seniority, etc.)
            balance.setYear(LocalDate.now().getYear());
            balance.setAccumulatedBalance(monthlyAccrual);
            balance.setUsedBalance(0);
            balance.setPreviousYearBalance(previousYearBalance);
            balance.setLastUpdated(LocalDateTime.now());

            employeeBalanceRepository.save(balance);
            totalOperations += 1;
        }
        balanceAccrualHistory.setTotalOperations(totalOperations);
        balanceAccrualRepository.save(balanceAccrualHistory);
    }
}
