package com.saham.hr_system.modules.leave.service.implementation;

import com.saham.hr_system.modules.leave.model.LeaveRequest;
import com.saham.hr_system.modules.leave.model.LeaveRequestStatus;
import com.saham.hr_system.modules.leave.repository.LeaveRequestRepository;
import com.saham.hr_system.modules.leave.service.LeaveRequestCanceller;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class LeaveRequestCancelerImpl implements LeaveRequestCanceller {

    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveRequestCancelerEmailSenderImpl leaveRequestCancelerEmailSenderImpl;

    private final static Logger log = LoggerFactory.getLogger(LeaveRequestCancelerImpl.class);

    @Autowired
    public LeaveRequestCancelerImpl(LeaveRequestRepository leaveRequestRepository, LeaveRequestCancelerEmailSenderImpl leaveRequestCancelerEmailSenderImpl) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.leaveRequestCancelerEmailSenderImpl = leaveRequestCancelerEmailSenderImpl;
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
        try{
            // fetch the request from db:
            LeaveRequest leaveRequest =
                    leaveRequestRepository.findByReferenceNumber(refNumber).orElseThrow();

            if(leaveRequest.getStatus().equals(LeaveRequestStatus.CANCELED)){
                log.warn("Leave request with reference number {} is already canceled.", refNumber);
                throw new IllegalStateException("Leave request is already canceled.");
            }
            // check if the request is already approved and became a leave:
            if(leaveRequest.getStatus().equals(LeaveRequestStatus.APPROVED)){
                log.warn("Leave request with reference number {} is already approved and cannot be canceled.", refNumber);
                throw new IllegalStateException("Leave request is already approved and cannot be canceled.");
            }

            leaveRequest.setStatus(LeaveRequestStatus.CANCELED);
            // save the request:
            leaveRequestRepository.save(leaveRequest);

            // notify the employee:
            CompletableFuture.runAsync(() ->
                    {
                        try {
                            leaveRequestCancelerEmailSenderImpl.notifyEmployee(leaveRequest);
                        } catch (MessagingException e) {
                            throw new RuntimeException(e);
                        }
                    }
            );

            // notify the manager:
            CompletableFuture.runAsync(() ->
                    {
                        try {
                            leaveRequestCancelerEmailSenderImpl.notifyManager(leaveRequest);
                        } catch (MessagingException e) {
                            throw new RuntimeException(e);
                        }
                    }
            );
        }catch (RuntimeException exception){
            log.error("Error while canceling leave request with reference number {}: {}", refNumber, exception.getMessage());
            throw exception;
        }
    }
}
