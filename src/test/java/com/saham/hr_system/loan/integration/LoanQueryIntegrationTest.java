package com.saham.hr_system.loan.integration;

import com.saham.hr_system.jwt.JwtUtilities;
import com.saham.hr_system.modules.loan.repository.LoanRequestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LoanQueryIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LoanRequestRepository loanRequestRepository;

    @Autowired
    private JwtUtilities jwtUtilities;

    @Test
    void testFetchLoanRequests() throws Exception {
        long count = loanRequestRepository.count();

        String token = jwtUtilities.generateToken("admin.hr@saham.com", List.of("HR", "ADMIN"));
        // Perform:
        /*         * We can use the count variable to assert the number of loan requests returned by the API.
         * For example, if we expect the API to return all loan requests, we can assert that the count matches the expected value.
         * If we want to test pagination, we can assert that the number of loan requests returned in the response matches the expected page size.
         */
        mockMvc
                .perform(get(
                        "/api/v1/loans/requests/hr/get-all"
                ).header("Authorization", String.format("Bearer %s", token))).andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    void testFetchPendingLoanRequests(){
        /*
        long count = loanRequestRepository.countByStatus(LoanRequestStatus.PENDING);

        // Perform:
        mockMvc
                .perform()

         */
    }
}
