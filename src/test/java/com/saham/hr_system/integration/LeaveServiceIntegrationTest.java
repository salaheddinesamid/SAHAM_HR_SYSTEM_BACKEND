package com.saham.hr_system.integration;

import com.saham.hr_system.HrSystemApplication;
import com.saham.hr_system.dto.LeaveRequestDto;

import com.saham.hr_system.dto.LeaveRequestResponse;
import com.saham.hr_system.model.Employee;
import com.saham.hr_system.repository.EmployeeBalanceRepository;
import com.saham.hr_system.repository.EmployeeRepository;
import com.saham.hr_system.repository.LeaveRequestRepository;
import com.saham.hr_system.service.implementation.LeaveServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = HrSystemApplication.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
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
    void testRequestLeave() throws Exception {

        Employee e = employeeRepository.findByEmail("Ciryane@saham.com").get();

        // Mock leave request dto:
        LeaveRequestDto request = new LeaveRequestDto();
        request.setStartDate(LocalDate.of(2025,11,30));
        request.setEndDate(LocalDate.of(2025,11,30));
        request.setType("");
        request.setComment("");

        assertEquals(0, leaveRequestRepository.findAllByEmployee(e).size());
        // Act:
        leaveService.requestLeave("Ciryane@saham.com",request);

        assertEquals(1, leaveRequestRepository.findAllByEmployee(e).size());

    }

    @Test
    void shouldThrowIfInsufficientBalance(){}

    @Test
    void shouldThrowEmployeeNotFound(){}

    @Test
    void testApproveLeaveRequestBySupervisor(){}

    @Test
    void shouldThrowRequestNotApprovedBySupervisor(){}

    @Test
    void testGetAllSubordinatesLeaveRequestShouldReturnOnlyInProcessAndNotApprovedByManager(){}
}
