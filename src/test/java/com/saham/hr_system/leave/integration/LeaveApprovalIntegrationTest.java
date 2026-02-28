package com.saham.hr_system.leave.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saham.hr_system.jwt.JwtUtilities;
import com.saham.hr_system.modules.leave.model.Leave;
import com.saham.hr_system.modules.leave.model.LeaveRequest;
import com.saham.hr_system.modules.leave.model.LeaveRequestStatus;
import com.saham.hr_system.modules.leave.repository.LeaveRequestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

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
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtilities jwtUtilities;


    @Test
    void testApproveSubordinateLeaveRequest() throws Exception {
        String token = jwtUtilities.generateToken("ceo@saham.com", List.of("MANAGER"));
        // Randomly fetch a leave request from the database that is pending approval and belongs to a subordinate of the manager.
        List<LeaveRequest> leaveRequests = leaveRequestRepository
                .findAllByStatus(LeaveRequestStatus.IN_PROCESS);
        LeaveRequest request = leaveRequests.get(0);
        Long requestId = request.getLeaveRequestId();

        // Perform a POST request to the endpoint /api/leaves/approve/{referenceNumber} with the reference number of the leave request.
        mockMvc.perform(
                put("/api/v1/leaves/requests/subordinates/approve-request")
                        .param("leaveRequestId", String.valueOf(6L))
                        .header("Authorization", "Bearer " + token)
        ).andDo(print())
                .andExpect(status().isOk());

        assertTrue(request.isApprovedByManager());
    }

    @Test
    void testApproveSubordinateLeaveRequestShouldThrowManagerNotAuthorized(){}

    @Test
    void testApproveSubordinateLeaveRequestAlreadyApproved(){}

    @Test
    void testApproveLeave() throws Exception {
        String token = jwtUtilities.generateToken("salaheddine.samid@saham.com", List.of("MANAGER", "HR"));
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
