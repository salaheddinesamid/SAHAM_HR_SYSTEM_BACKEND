package com.saham.hr_system.leave.unit;

import com.saham.hr_system.modules.leave.repository.LeaveRequestRepository;
import com.saham.hr_system.modules.leave.service.implementation.DefaultLeaveRequestProcessor;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

public class DefaultLeaveRequestProcessorUnitTest {

    @InjectMocks
    private LeaveRequestRepository leaveRequestRepository;

    @InjectMocks
    private DefaultLeaveRequestProcessor defaultLeaveRequestProcessor;

    @Test
    void testProcessAnnualLeaveRequestSuccess(){}

    @Test
    void testProcessAnnualLeaveRequestThrowsEmployeeNotFound(){}
}
