package com.saham.hr_system.leave.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saham.hr_system.jwt.JwtUtilities;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.modules.leave.model.Leave;
import com.saham.hr_system.modules.leave.model.LeaveRequest;
import com.saham.hr_system.modules.leave.model.LeaveRequestStatus;
import com.saham.hr_system.modules.leave.model.LeaveType;
import com.saham.hr_system.modules.leave.repository.LeaveRequestRepository;
import com.saham.hr_system.modules.leave.utils.LeaveRequestRefNumberGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")

public class LeaveApprovalIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private LeaveRequestRefNumberGenerator leaveRequestRefNumberGenerator;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtilities jwtUtilities;


    @Test
    void testApproveSubordinateLeaveRequest() throws Exception {
        // Generate JWT token
        String token = jwtUtilities.generateToken("ceo@saham.com", List.of("MANAGER"));

        // Prepare a pending leave request
        LeaveRequest request = leaveRequestRepository.findAllByStatus(LeaveRequestStatus.IN_PROCESS)
                .stream()
                .findFirst()
                .orElseGet(this::mockLeaveRequest);

        // Perform the approval endpoint
        mockMvc.perform(
                        put("/api/v1/leaves/requests/subordinates/approve-request")
                                .param("leaveRequestId", String.valueOf(request.getLeaveRequestId()))
                                .header("Authorization", "Bearer " + token)
                ).andDo(print())
                .andExpect(status().isOk());

        // Reload from DB after service call
        LeaveRequest updatedRequest = leaveRequestRepository.findById(request.getLeaveRequestId())
                .orElseThrow();

        // Assert that it was approved
        assertTrue(updatedRequest.isApprovedByManager());
    }

    private LeaveRequest mockLeaveRequest(){
        LeaveRequest request = new LeaveRequest();
        request.setStatus(LeaveRequestStatus.IN_PROCESS);
        request.setApprovedByManager(false);
        request.setEmployee(employeeRepository.findById(1L).orElseThrow());
        request.setTypeOfLeave(LeaveType.ANNUAL);
        request.setStartDate(LocalDate.of(2026, 4, 20));
        request.setEndDate(LocalDate.of(2026, 4, 25));

        request.setReferenceNumber(leaveRequestRefNumberGenerator.generate(request));

        // Set required relationships if necessary
        // e.g., request.setEmployee(employeeRepository.findByEmail("subordinate@saham.com"));

        return leaveRequestRepository.save(request); // auto-generated ID
    }

    @Test
    void testApproveSubordinateLeaveRequestShouldThrowManagerNotAuthorized(){}

    @Test
    void testApproveSubordinateLeaveRequestAlreadyApproved(){}

    @Test
    void testApproveLeave() throws Exception {
        String token = jwtUtilities.generateToken("admin.hr@saham.com", List.of("EMPLOYEE", "HR"));
        // Randomly fetch a leave request from the database that is pending approval and belongs to a subordinate of the manager.
        List<LeaveRequest> leaveRequests = leaveRequestRepository
                .findAllByStatus(LeaveRequestStatus.IN_PROCESS);
        LeaveRequest request = leaveRequests.get(0);
        Long requestId = request.getLeaveRequestId();

        // Perform a POST request to the endpoint /api/leaves/approve/{referenceNumber} with the reference number of the leave request.
        mockMvc.perform(
                        put("/api/v1/leaves/requests/hr/approve")
                                .param("leaveRequestId", String.valueOf(6L))
                                .header("Authorization", "Bearer " + token)
                ).andDo(print())
                .andExpect(status().isOk());

        assertTrue(request.isApprovedByManager());
    }

    @Test
    void testRejectSubordinateLeaveRequest(){}

    @Test
    void testRejectLeave(){}
}
