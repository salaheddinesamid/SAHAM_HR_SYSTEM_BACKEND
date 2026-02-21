package com.saham.hr_system.loan.integration;

import com.saham.hr_system.jwt.JwtUtilities;
import com.saham.hr_system.modules.loan.model.LoanRequest;
import com.saham.hr_system.modules.loan.model.LoanRequestStatus;
import com.saham.hr_system.modules.loan.repository.LoanRequestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LoanApprovalIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LoanRequestRepository loanRequestRepository;

    @Autowired
    private JwtUtilities jwtUtilities;
    /*
    @Test
    void testApproveLoanRequest() throws Exception {
        LoanRequest loanRequest = loanRequestRepository.findByStatus(LoanRequestStatus.IN_PROCESS)
                .orElseThrow(()-> new RuntimeException("No loan request in process found for testing"));
        String token = jwtUtilities.generateToken("salaheddine@saham.com", List.of("HR"));

        // Perform
        mockMvc.perform(
                put("/api/v1/loans/requests/hr/approve-request")
                        .header("Authorization", "Bearer " + token)
                        .param("requestId", loanRequest.getRequestId().toString())
        ).andDo(print())
                .andExpect(status().isOk());
        // Verify that the loan request is approved and the status is updated in the database
        assertEquals(LoanRequestStatus.APPROVED, loanRequest.getStatus());
    }

     */
}
