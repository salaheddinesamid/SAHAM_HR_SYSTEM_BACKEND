package com.saham.hr_system.loan.integration;

import com.saham.hr_system.modules.loan.repository.LoanRequestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LoanQueryIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LoanRequestRepository loanRequestRepository;

    @Test
    void testFetchLoanRequests(){
        long count = loanRequestRepository.count();

        // Perform:
        /*         * We can use the count variable to assert the number of loan requests returned by the API.
         * For example, if we expect the API to return all loan requests, we can assert that the count matches the expected value.
         * If we want to test pagination, we can assert that the number of loan requests returned in the response matches the expected page size.
         */
        //mockMvc
                //.perform(get())

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
