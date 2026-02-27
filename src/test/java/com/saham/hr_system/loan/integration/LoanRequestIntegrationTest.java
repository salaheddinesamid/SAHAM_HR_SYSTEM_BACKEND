package com.saham.hr_system.loan.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saham.hr_system.jwt.JwtUtilities;
import com.saham.hr_system.modules.loan.dto.LoanRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LoanRequestIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtilities jwtUtilities;

    @Test
    void testCreateNormalLoanRequest() throws Exception {

        String token = jwtUtilities.generateToken("salaheddine.samid@saham.com", List.of("EMPLOYEE"));
        // Mock DTO
        LoanRequestDto loanRequestDto = new LoanRequestDto(
                "NORMAL",
                5000.0,
                "Need funds for home renovation",
                LocalDate.of(2026, 12, 31)
        );

        // Perform POST request to create loan request
        mockMvc.perform(
                post("/api/v1/loans/apply")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loanRequestDto))
        ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testCreateAdvancedLoanRequest(){}
}
