package com.saham.hr_system.modules.leave.service.implementation;

import com.saham.hr_system.modules.leave.model.LeaveRequest;
import com.saham.hr_system.modules.leave.model.LeaveRequestStatus;
import com.saham.hr_system.modules.leave.repository.LeaveRequestRepository;
import com.saham.hr_system.modules.leave.service.LeaveRequestCanceller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LeaveRequestCancelerImpl implements LeaveRequestCanceller {

    private final LeaveRequestRepository leaveRequestRepository;

    @Autowired
    public LeaveRequestCancelerImpl(LeaveRequestRepository leaveRequestRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
    }

    /**
     * Here the leave request status must be in process only.
     * @param status
     * @return
     */
    @Override
    public boolean supports(String status) {
        return LeaveRequestStatus.IN_PROCESS.equals(LeaveRequestStatus.valueOf(status));
    }

    /**
     *
     * @param id
     */
    @Override
    public void cancel(Long id) {
        // fetch the request from db:
        LeaveRequest leaveRequest =
                leaveRequestRepository.findById(id).orElseThrow();
        // set the status to cancel:
        leaveRequest.setStatus(LeaveRequestStatus.CANCELED);

        // save the request:
        leaveRequestRepository.save(leaveRequest);
    }
}
