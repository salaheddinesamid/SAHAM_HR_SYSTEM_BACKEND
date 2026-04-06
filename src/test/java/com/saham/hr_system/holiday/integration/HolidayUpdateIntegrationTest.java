package com.saham.hr_system.holiday.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saham.hr_system.modules.holidays.dto.HolidayModificationDto;
import com.saham.hr_system.modules.holidays.model.Holiday;
import com.saham.hr_system.modules.holidays.repository.HolidayRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class HolidayUpdateIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HolidayRepository holidayRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testUpdateReligiousHolidaySuccess() throws Exception {
        // Mock update request DTO
        HolidayModificationDto requestDto = new HolidayModificationDto(
                "Aïd el-Fitr (prévisionnel)",
                LocalDate.of(2026, 4, 11),
                LocalDate.of(2026, 4, 13),
                2
        );

        // Perform the update request and assert the response:
        mockMvc
                .perform(patch("/api/v1/holidays/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .param("id", String.valueOf(4L))
                        .param("type","RELIGIOUS")
                ).andDo(print());
    }
}
