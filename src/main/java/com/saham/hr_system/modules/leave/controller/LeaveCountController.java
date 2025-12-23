package com.saham.hr_system.modules.leave.controller;

import com.saham.hr_system.modules.leave.dto.NewLeaveRequestsCountMessage;
import com.saham.hr_system.modules.leave.service.implementation.LeaveRequestQueryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class LeaveCountController {
    private final LeaveRequestQueryImpl leaveRequestQuery;

    @Autowired
    public LeaveCountController(LeaveRequestQueryImpl leaveRequestQuery) {
        this.leaveRequestQuery = leaveRequestQuery;
    }

    @MessageMapping("/subordinates-count/{managerEmail}")
    @SendTo("/topic/subordinates-leave-count")
    public NewLeaveRequestsCountMessage sendSubordinatesLeaveCountUpdate(
            @DestinationVariable String managerEmail
    ) {
        // In a real application, you would process the message and fetch updated counts.
        long content = leaveRequestQuery.getNumberOfInProcessSubordinatesLeaveRequests(
                managerEmail
        );

        return new NewLeaveRequestsCountMessage(content);
    }
}
