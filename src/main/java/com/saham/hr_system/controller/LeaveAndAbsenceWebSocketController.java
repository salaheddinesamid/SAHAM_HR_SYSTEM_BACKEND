package com.saham.hr_system.controller;

import com.saham.hr_system.modules.leave.service.implementation.LeaveRequestQueryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class LeaveAndAbsenceWebSocketController {

    private final LeaveRequestQueryImpl leaveRequestQuery;

    @Autowired
    public LeaveAndAbsenceWebSocketController(LeaveRequestQueryImpl leaveRequestQuery) {
        this.leaveRequestQuery = leaveRequestQuery;
    }

    @MessageMapping("/leave-requests-count-subordinates")
    @SendTo("")
    public long getLeaveRequestsCountForSubordinates(@Header("email") String managerEmail) {
        // This is a placeholder implementation.
        // In a real application, you would fetch the count from the service layer.
        return leaveRequestQuery
                .getNumberOfInProcessSubordinatesLeaveRequests(managerEmail);
    }
}
