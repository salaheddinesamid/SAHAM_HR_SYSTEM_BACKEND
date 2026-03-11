package com.saham.hr_system.employee.integration;

import com.saham.hr_system.modules.employees.model.EmployeeBalance;
import com.saham.hr_system.modules.employees.repository.EmployeeBalanceRepository;
import com.saham.hr_system.modules.employees.service.implementation.EmployeeBalanceAccrualServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")

public class EmployeeBalanceAccrualServiceIntegrationTest {

    @Autowired
    private EmployeeBalanceAccrualServiceImpl employeeBalanceAccrualService;

    @Autowired
    private EmployeeBalanceRepository employeeBalanceRepository;

    @Test
    void testMonthlyAccrualSuccess(){
        EmployeeBalance employeeBalance = employeeBalanceRepository.findAll().get(0);
        // Act and verify
        employeeBalanceAccrualService.processMonthlyAccruals();

        Assertions.assertEquals(32.5, employeeBalance.getAccumulatedBalance());
    }

    @Test
    void testYearlyAccrualSuccess(){
        employeeBalanceAccrualService.processYearlyAccruals();
    }
}
