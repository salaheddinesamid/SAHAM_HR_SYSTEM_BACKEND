package com.saham.hr_system.payroll.integration;

import com.saham.hr_system.HrSystemApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = HrSystemApplication.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Slf4j
public class PayrollServiceIntegrationTest{

    @Test
    void testUploadPayrollSuccess(){}
}
