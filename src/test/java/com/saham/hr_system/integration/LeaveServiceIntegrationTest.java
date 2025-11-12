package com.saham.hr_system.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saham.hr_system.HrSystemApplication;
import com.saham.hr_system.dto.LeaveRequestDto;
import com.saham.hr_system.model.Employee;
import com.saham.hr_system.model.EmployeeBalance;
import com.saham.hr_system.model.LeaveRequest;
import com.saham.hr_system.repository.EmployeeBalanceRepository;
import com.saham.hr_system.repository.EmployeeRepository;
import com.saham.hr_system.repository.LeaveRequestRepository;
import com.saham.hr_system.service.implementation.LeaveServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This class contains integration test for leave functional requirements.
 * It tests all the layers (controller, service, repository).
 */
@SpringBootTest(classes = HrSystemApplication.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Slf4j
public class LeaveServiceIntegrationTest {


    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeBalanceRepository employeeBalanceRepository;

    @Autowired
    private LeaveServiceImpl leaveService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp(){
    }

    @Test
    void testData(){
        assertEquals(false, employeeRepository.findAll().isEmpty());
        assertNotNull(employeeRepository.findByEmail("Ciryane@saham.com"));
    }

    @Test
    void testRequestLeaveIntegration() throws Exception {

        LeaveRequestDto requestDto = new LeaveRequestDto();
        requestDto.setStartDate(LocalDate.of(2025, 11, 30));
        requestDto.setEndDate(LocalDate.of(2025, 12, 5));
        requestDto.setType("ANNUAL");
        requestDto.setComment("Vacation");


        mockMvc.perform(post("/api/v1/leaves/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .param("email", "Ciryane@saham.com"))
                .andExpect(status().isOk());

        assertEquals(4, leaveRequestRepository.findAll().size());
    }

    @Test
    void shouldThrowIfInsufficientBalance() throws Exception {

        LeaveRequestDto requestDto = new LeaveRequestDto();
        requestDto.setStartDate(LocalDate.of(2025, 11, 30));
        requestDto.setEndDate(LocalDate.of(2025, 12, 5));
        requestDto.setType("ANNUAL");
        requestDto.setComment("Vacation");

        // Assuming the employee has no enough balance left:
        Employee e = employeeRepository
                .findByEmail("Ciryane@saham.com").orElse(null);
        EmployeeBalance employeeBalance = employeeBalanceRepository
                .findByEmployee(e).orElse(null);
        assert employeeBalance != null;
        employeeBalance.setDaysLeft(0);
        employeeBalanceRepository.save(employeeBalance);

        // Act:
        mockMvc.perform(post("/api/v1/leaves/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .param("email", "Ciryane@saham.com"))
                .andExpect(status().isBadRequest());

    }

    @Test
    void shouldThrowEmployeeNotFound(){}

    @Test
    void testApproveLeaveRequestBySupervisor() throws Exception {


        // Assuming there is a subordinate leave request in the database: (subordinate of Ciryane)
        Employee manager = employeeRepository
                .findByEmail("Ciryane@saham.com").orElseThrow();

        List<Employee> subordinates = employeeRepository
                .findAllByManagerId(manager.getId());
        // Choose subordinate for test
        Employee sub = subordinates.get(0);

        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setStartDate(LocalDate.of(2025, 11, 30));
        leaveRequest.setEndDate(LocalDate.of(2025, 12, 5));
        leaveRequest.setEmployee(sub);
        leaveRequest.setRequestDate(LocalDateTime.now());
        leaveRequest.setTypeOfLeave("");
        leaveRequest.setApprovedByManager(true);
        leaveRequest.setApprovedByHr(false);

        LeaveRequest savedRequest =  leaveRequestRepository.save(leaveRequest);

        // Act:
        Long id = savedRequest.getLeaveRequestId();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("email", "Ciryane@saham.com");
        params.add("leaveRequestId", String.valueOf(id));
        mockMvc.perform(put("/api/v1/leaves/subordinates/approve-request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .params(params))
                .andExpect(status().isOk());


    }

    @Test
    void testRejectLeaveRequestBySupervisor() throws Exception {
        // Assuming there is a subordinate leave request in the database: (subordinate of Ciryane)
        Employee manager = employeeRepository
                .findByEmail("Ciryane@saham.com").orElseThrow();

        List<Employee> subordinates = employeeRepository
                .findAllByManagerId(manager.getId());
        // Choose subordinate for test
        Employee sub = subordinates.get(0);

        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setStartDate(LocalDate.of(2025, 11, 30));
        leaveRequest.setEndDate(LocalDate.of(2025, 12, 5));
        leaveRequest.setEmployee(sub);
        leaveRequest.setRequestDate(LocalDateTime.now());
        leaveRequest.setTypeOfLeave("");
        leaveRequest.setApprovedByManager(false);
        leaveRequest.setApprovedByHr(false);

        LeaveRequest savedRequest =  leaveRequestRepository.save(leaveRequest);

        // Act:
        Long id = savedRequest.getLeaveRequestId();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("email", "Ciryane@saham.com");
        params.add("leaveRequestId", String.valueOf(id));
        mockMvc.perform(put("/api/v1/leaves/subordinates/reject-request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .params(params))
                .andExpect(status().isOk());
    }

    @Test
    void shouldThrowRequestNotApprovedBySupervisor(){}

    @Test
    void testGetAllSubordinatesLeaveRequestShouldReturnOnlyInProcessAndNotApprovedByManager() throws Exception {

        // Act:
        mockMvc
                .perform(get("/api/v1/leaves/subordinates/get_all_requests")
                        .param("email", "Ciryane@saham.com")
                ).andExpect(status().isOk());
    }
}
