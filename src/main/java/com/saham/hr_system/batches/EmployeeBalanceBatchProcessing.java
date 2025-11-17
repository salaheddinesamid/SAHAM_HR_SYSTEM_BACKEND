package com.saham.hr_system.batches;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * This class is a batch processing component that is responsible for handling employee balance processing tasks.
 * Every 1st January at midnight, it triggers the processing of employee balances.
 * */
@Component
public class EmployeeBalanceBatchProcessing {

    @Scheduled(cron = "0 0 0 1 1 *") // Runs at midnight on January 1st every year
    private void processEmployeeBalances(){

    }

    private void processCumulatedBalances(){
    }
    private void processYearlyBalances(){}
}
