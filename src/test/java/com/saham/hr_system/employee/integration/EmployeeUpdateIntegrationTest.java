package com.saham.hr_system.employee.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saham.hr_system.modules.employees.dto.UpdateEmployeeDto;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EmployeeUpdateIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testUpdateEmployeeDetails() throws Exception {
        // Given: An existing employee in the database
        Long employeeId = 5L; // Assuming an employee with ID 1 exists

        // Mock DTO
        UpdateEmployeeDto employeeDto = new UpdateEmployeeDto(
                "AMINE",
                "Samid",
                null,
                null,
                null,
                LocalDate.of(2003,12, 3),
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
        // When: We perform a PUT request to update the employee's details
        // (You would need to implement the actual request here, using mockMvc)
        mockMvc.perform(
                patch("/api/v1/employees/update/"+employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDto))
        ).andDo(print())
                .andExpect(status().isOk());
        // Then: We verify that the employee's details have been updated in the database
        // (You would need to implement the actual verification here, using assertions)
    }
}
