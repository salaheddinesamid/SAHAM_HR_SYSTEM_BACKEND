package com.saham.hr_system.employee.integration;

import com.saham.hr_system.modules.employees.model.EmployeeBalance;
import com.saham.hr_system.modules.employees.repository.EmployeeBalanceRepository;
import com.saham.hr_system.modules.employees.service.implementation.EmployeeBalanceAccrualServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")

public class       EmployeeBalanceAccrualServiceIntegrationTest {

    @Autowired
    private EmployeeBalanceAccrualServiceImpl employeeBalanceAccrualService;

    @Autowired
    private EmployeeBalanceRepository employeeBalanceRepository;

    @Test
    void testMonthlyAccrualSuccess(){
        EmployeeBalance employeeBalance = employeeBalanceRepository.findAll().get(0);
        double initialAccumulatedBalance = employeeBalance.getAccumulatedBalance();
        // Act and verify
        employeeBalanceAccrualService.processMonthlyAccruals();
        double newAccumulatedBalance = employeeBalance.getAccumulatedBalance();

        // Reload from the database
        employeeBalance = employeeBalanceRepository.findById(employeeBalance.getBalanceId()).orElseThrow();
        log.info("Initial accumulated balance: {}, Monthly balance: {}, New accumulated balance: {}",
                initialAccumulatedBalance, employeeBalance.getMonthlyBalance(), newAccumulatedBalance);

        Assertions.assertEquals(initialAccumulatedBalance + employeeBalance.getMonthlyBalance(), employeeBalance.getAccumulatedBalance());
    }

    @Test
    void testYearlyAccrualSuccess(){
        employeeBalanceAccrualService.processYearlyAccruals();
    }
}
