package com.saham.hr_system.employee.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saham.hr_system.jwt.JwtUtilities;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
public class EmployeeQueryIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private JwtUtilities jwtUtilities;

    @Test
    void testFetchAllEmployees() throws Exception {

        // Generate access token
        String accessToken = jwtUtilities.generateToken("admin.hr@saham.com", List.of("ADMIN", "EMPLOYEE"));
        mockMvc.perform(get("/api/v1/employees/get_all").header("Authorization", "Bearer " + accessToken))
                .andDo(print()) // optional: prints response to console
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(employeeRepository.count())); // expects 3 employees

    }
}
