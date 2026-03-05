package com.saham.hr_system.batches;

import com.saham.hr_system.modules.employees.service.implementation.EmployeeBalanceAccrualServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Month;

/**
 *
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EmployeesBalanceBatch {
    private final EmployeeBalanceAccrualServiceImpl employeeBalanceAccrualService;

    @Scheduled(cron = "0 0 0 1 * ?")
    private void runMonthlyAccruals(){
        if(!LocalDate.now().getMonth().equals(Month.JANUARY)){
            log.info("Starting monthly accruals batch job at {}", System.currentTimeMillis());
            employeeBalanceAccrualService.processMonthlyAccruals();
            log.info("Finished monthly accruals batch job at {}", System.currentTimeMillis());
        }
    }

    @Scheduled(cron = "0 0 0 1 1 ?")
    private void runYearlyAccruals() {
        log.info("Starting yearly accruals batch job at {}", System.currentTimeMillis());
        employeeBalanceAccrualService.processYearlyAccruals();
        log.info("Finished yearly accruals batch job at {}", System.currentTimeMillis());
    }
}
