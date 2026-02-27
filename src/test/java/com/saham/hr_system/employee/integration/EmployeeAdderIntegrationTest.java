package com.saham.hr_system.employee.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saham.hr_system.modules.employees.dto.*;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
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
                "SDT123456D",
                "Software Engineer",
                "IT",
                "SAHAM_HORIZON",
                2L,
                LocalDate.of(2025,11,22),
                "Casablanca",
                "00",
                "salaheddine.samid@saham.com",
                "00",
                "00"
        );
        NewEmployeeSocialDetails socialDetailsDto = new NewEmployeeSocialDetails(
                "CNSS6723456",
                "CIMR983456",
                "INSCR123456",
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
                "Amine",
                "Samid",
                "MALE",
                "D573GHSD",
                "Wafaa 01, NR 11 Hay Salam, Casablanca",
                "Morocco",
                LocalDate.of(2000, 1, 1),
                "SINGLE",
                0,
                "salaheddine.samid@saham.com",
                professionalDetailsDto,
                socialDetailsDto,
                contactDetails,
                List.of("EMPLOYEE", "MANAGER"),
                balanceDto
        );

        if(!employeeRepository.existsByEmployeeProfessionalDetails_Matriculation(
                newEmployeeDto.getProfessionalDetailsDto().getMatriculation()
        )){
            mockMvc.perform(post("/api/v1/employees/new")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(newEmployeeDto)))
                    .andDo(print())
                    .andExpect(status().isOk());
        }
        assertTrue(employeeRepository.existsByEmail("salaheddine.samid@saham.com"));
    }
}

