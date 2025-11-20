package com.saham.hr_system.modules.leave.service.implementation;

import com.saham.hr_system.modules.leave.model.LeaveRequest;
import com.saham.hr_system.modules.leave.model.LeaveRequestStatus;
import com.saham.hr_system.modules.leave.repository.LeaveRequestRepository;
import com.saham.hr_system.modules.leave.service.LeaveRequestCanceler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LeaveRequestCancelerImpl implements LeaveRequestCanceler {

    private final LeaveRequestRepository leaveRequestRepository;

    @Autowired
    public LeaveRequestCancelerImpl(LeaveRequestRepository leaveRequestRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
    }

    @Override
    public void cancelLeaveRequest(Long requestId) {
        // fetch the request from db:
        LeaveRequest leaveRequest = leaveRequestRepository.findById(requestId).orElseThrow();

        // set status to canceled
        leaveRequest.setStatus(LeaveRequestStatus.CANCELED);

        // save the leave request:
        leaveRequestRepository.save(leaveRequest);
    }
}
