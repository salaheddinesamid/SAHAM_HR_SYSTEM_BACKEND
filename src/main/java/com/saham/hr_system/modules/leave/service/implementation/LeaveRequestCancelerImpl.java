package com.saham.hr_system.modules.leave.service.implementation;

import com.saham.hr_system.modules.leave.model.LeaveRequest;
import com.saham.hr_system.modules.leave.model.LeaveRequestStatus;
import com.saham.hr_system.modules.leave.repository.LeaveRepository;
import com.saham.hr_system.modules.leave.repository.LeaveRequestRepository;
import com.saham.hr_system.modules.leave.service.LeaveRequestCanceller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LeaveRequestCancelerImpl implements LeaveRequestCanceller {

    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveCancellerImpl leaveCancellerImpl;

    @Autowired
    public LeaveRequestCancelerImpl(LeaveRequestRepository leaveRequestRepository, LeaveRepository leaveRepository, LeaveCancellerImpl leaveCancellerImpl) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.leaveCancellerImpl = leaveCancellerImpl;
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
     * @param refNumber
     */
    @Override
    public void cancel(String refNumber) {
        // fetch the request from db:
        LeaveRequest leaveRequest =
                leaveRequestRepository.findByReferenceNumber(refNumber).orElseThrow();

        if(leaveRequest.getStatus().equals(LeaveRequestStatus.CANCELED)){
            throw new IllegalStateException("Leave request is already canceled.");
        }
        // check if the request is already approved and became a leave:
        if(leaveRequest.getStatus().equals(LeaveRequestStatus.APPROVED)){
            // set the request to cancel:
            leaveRequest.setStatus(LeaveRequestStatus.CANCELED);
            // save the request:
            leaveRequestRepository.save(leaveRequest);
            // call the leave canceler:
            leaveCancellerImpl.cancel(leaveRequest.getReferenceNumber());
        }


    }
}
