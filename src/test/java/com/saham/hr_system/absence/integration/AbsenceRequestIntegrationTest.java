package com.saham.hr_system.absence.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saham.hr_system.jwt.JwtUtilities;
import com.saham.hr_system.modules.absence.dto.AbsenceRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")

public class AbsenceRequestIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtilities jwtUtilities;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateRemoteWorkAbsenceRequest() throws Exception {
        // Generate JWT token
        String token = jwtUtilities.generateToken("admin.hr@saham.com", List.of("EMPLOYEE", "ADMIN"));
        mockMvc.perform(
                        multipart("/api/v1/absences/new")
                                .param("email", "admin.hr@saham.com")
                                .param("type", "REMOTE_WORK")
                                .param("startDate", "2026-01-01")
                                .param("endDate", "2026-01-05")
                                .header("Authorization", String.format("Bearer %s", token))
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    /*
    @Test
    void testCreateSicknessAbsenceRequest() throws Exception {
        // Mock MultipartFile for medical certificate
        String content = "This is the file content.";
        MockMultipartFile mockFile = new MockMultipartFile(
                "fileParamName", // The name of the parameter in the form
                "originalFileName.txt", // The original file name
                "text/plain", // Content type
                content.getBytes() // File content as bytes
        );
        // Mock DTO
        AbsenceRequestDto absenceRequestDto = new AbsenceRequestDto(
                "SICKNESS",
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 5),
                mockFile
        );
        mockMvc.perform(
                        multipart("/api/v1/absences/new")
                                .file(mockFile)
                                .param("email", "salaheddine@saham.com")
                                .param("type", "SICKNESS")
                                .param("startDate", "2026-01-01")
                                .param("endDate", "2026-01-05")
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

     */
}
