package com.saham.hr_system.leave.unit;

import com.saham.hr_system.modules.leave.repository.LeaveRequestRepository;
import com.saham.hr_system.modules.leave.service.implementation.LeaveRequestQueryImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class LeaveRequestQueryServiceUnitTest {

    @Mock
    private LeaveRequestRepository leaveRequestRepository;

    @InjectMocks
    private LeaveRequestQueryImpl leaveRequestQueryImpl;

    @Test
    void testFetchAllEmployeeLeaveRequestHistory(){}

    @Test
    void testFetchAllLeaveRequestsForManager(){}

    @Test
    void testFetchAllLeaveRequestsForHR(){}
}
