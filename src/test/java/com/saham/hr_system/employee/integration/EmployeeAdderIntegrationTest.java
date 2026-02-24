package com.saham.hr_system.employee.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saham.hr_system.modules.employees.dto.*;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EmployeeAdderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddNewEmployee() throws Exception {

        NewEmployeeProfessionalDetailsDto professionalDetailsDto = new NewEmployeeProfessionalDetailsDto(
                "MAT123456",
                "Software Engineer",
                "IT",
                "SAHAM_HORIZON",
                4L,
                LocalDate.of(2025,11,22),
                "Casablanca",
                "00",
                "salaheddine@saham.com",
                "00",
                "00"
        );
        NewEmployeeSocialDetails socialDetailsDto = new NewEmployeeSocialDetails(
                "CNSS123456",
                "CIMR123456",
                "INS123456",
                ""
        );
        NewEmployeeContactDetails contactDetails = new NewEmployeeContactDetails(
                "Dad",
                "+212612345678"
        );
        EmployeeBalanceDto balanceDto = new EmployeeBalanceDto(
                2025,
                25,
                25,
                2,
                0
        );
        NewEmployeeDto newEmployeeDto = new NewEmployeeDto(
                "Salaheddine",
                "Samid",
                "MALE",
                "T573GH",
                LocalDate.of(2000, 1, 1),
                "SINGLE",
                0,
                "salaheddine@saham.com",
                professionalDetailsDto,
                socialDetailsDto,
                contactDetails,
                List.of("EMPLOYEE", "MANAGER"),
                balanceDto
        );

        mockMvc.perform(post("/api/v1/employees/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEmployeeDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}

