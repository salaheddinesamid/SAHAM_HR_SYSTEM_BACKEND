package com.saham.hr_system.loan.integration;

import com.saham.hr_system.modules.loan.repository.LoanRequestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LoanRejectionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LoanRequestRepository loanRequestRepository;

    @Test
    void testRejectLoanRequestSuccess(){}
}
