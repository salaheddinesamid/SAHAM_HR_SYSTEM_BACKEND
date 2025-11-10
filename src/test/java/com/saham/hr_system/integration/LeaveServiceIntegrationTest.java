package com.saham.hr_system.integration;

import com.saham.hr_system.HrSystemApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = HrSystemApplication.class)
@ActiveProfiles("test")
public class LeaveServiceIntegrationTest {

    @Test
    void testRequestLeave(){}

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
