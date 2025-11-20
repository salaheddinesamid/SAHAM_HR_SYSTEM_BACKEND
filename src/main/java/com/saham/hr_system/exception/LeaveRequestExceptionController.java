package com.saham.hr_system.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class LeaveRequestExceptionController {

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<?> handleInsufficientBalanceException(InsufficientBalanceException ex) {
        return
                ResponseEntity.badRequest().body("Insufficient leave balance: " + ex.getMessage());
    }

    @ExceptionHandler(LeaveRequestNotApprovedBySupervisorException.class)
    public ResponseEntity<?> handleLeaveRequestNotApprovedBySupervisorException(LeaveRequestNotApprovedBySupervisorException ex) {
        return
                ResponseEntity.badRequest().body("Leave request not approved by supervisor: " + ex.getMessage());
    }

    @ExceptionHandler(LeaveRequestAlreadyApprovedException.class)
    public ResponseEntity<?> handleLeaveRequestAlreadyApprovedByManagerException(LeaveRequestAlreadyApprovedException ex) {
        return
                ResponseEntity.
                        status(409) // Conflict
                        .body("Leave request already approved : " + ex.getMessage());
    }

    @ExceptionHandler(LeaveRequestAlreadyRejectedException.class)
    public ResponseEntity<?> handleLeaveRequestAlreadyRejectedException(LeaveRequestAlreadyRejectedException ex) {
        return
                ResponseEntity.
                        status(409) // Conflict
                        .body("Leave request already rejected : " + ex.getMessage());
    }
}
