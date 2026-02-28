package com.saham.hr_system.leave.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saham.hr_system.jwt.JwtUtilities;
import com.saham.hr_system.modules.leave.dto.LeaveRequestDto;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LeaveRequestIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtilities jwtUtilities;

    @Test
    void testApplyLeaveRequest() throws Exception {
        String token = jwtUtilities.generateToken("salaheddine.samid@saham.com", List.of("EMPLOYEE"));
        // Mock the DTO
        LeaveRequestDto leaveRequestDto = new LeaveRequestDto(
                LocalDate.of(2026, 4, 10),
                LocalDate.of(2026, 4, 12),
                "ANNUAL",
                null,
                "Vacation"
        );
        mockMvc.perform(
                post("/api/v1/leaves/apply")
                        .header("Authorization", "Bearer " + token)
                        .param("email", "salaheddine.samid@saham.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(leaveRequestDto))
        ).andExpect(status().isOk());
    }

    @Test
    void testApplyLeaveRequestMissingFieldsOrInvalidDates() throws Exception {
        String token = jwtUtilities.generateToken("salaheddine.samid@saham.com", List.of("EMPLOYEE"));
        // Mock the DTO
        LeaveRequestDto leaveRequestDto = new LeaveRequestDto(
                LocalDate.of(2026, 7, 10),
                LocalDate.of(2026, 7, 1),
                "ANNUAL",
                null,
                "Vacation"
        );
        mockMvc.perform(
                post("/api/v1/leaves/apply")
                        .header("Authorization", "Bearer " + token)
                        .param("email", "salaheddine@saham.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(leaveRequestDto))
        ).andExpect(status().isBadRequest());
    }
}
