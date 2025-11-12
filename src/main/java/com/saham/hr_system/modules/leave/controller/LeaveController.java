package com.saham.hr_system.modules.leave.controller;

import com.saham.hr_system.modules.leave.dto.LeaveRequestDto;
import com.saham.hr_system.modules.leave.dto.LeaveRequestResponse;
import com.saham.hr_system.modules.leave.service.implementation.LeaveServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/leaves")
public class LeaveController {

    private final LeaveServiceImpl leaveService;

    public LeaveController(LeaveServiceImpl leaveService) {
        this.leaveService = leaveService;
    }

    @PostMapping("apply")
    public ResponseEntity<?> applyForLeave(@RequestParam String email,
                                           @RequestBody LeaveRequestDto leaveRequestDto
                                           ){
        leaveService.requestLeave(email,leaveRequestDto);
        return ResponseEntity
                .status(200)
                .body("Leave applied successfully");
    }

    @GetMapping("get")
    public ResponseEntity<?> getLeaves(@RequestParam String email){
        return ResponseEntity
                .status(200)
                .body(leaveService.getAllLeaveRequests(email));
    }

    @GetMapping("hr/get_all_requests")
    public ResponseEntity<?> getAllRequestsForHR(){

        List<LeaveRequestResponse> requests = leaveService.getAllLeaveRequestsForHR();

        return ResponseEntity.status(200)
                .body(requests);
    }

    // Manager approval
    @PutMapping("subordinates/approve-request")
    public ResponseEntity<?> approveSubordinateRequest(@RequestParam Long leaveRequestId) {
        leaveService.approveSubordinateLeaveRequest(leaveRequestId);
        return
                ResponseEntity
                        .status(200)
                        .body(Map.of("message","Leave request has been approved"));
    }

    // Manager rejection
    @PutMapping("subordinates/reject-request")
    public ResponseEntity<?> rejectSubordinateRequest(@RequestParam Long leaveRequestId) {
        leaveService.rejectSubordinateLeaveRequest(leaveRequestId);
        return
                ResponseEntity
                        .status(200)
                        .body(Map.of("message","Leave request has been rejected"));
    }

    // Final approval by HR
    @PutMapping("approve-request")
    public ResponseEntity<?> approveRequest(@RequestParam Long leaveRequestId) {
        leaveService.approveLeaveRequest(leaveRequestId);
        return
                ResponseEntity
                        .status(200)
                        .body(Map.of("message","Leave request has been finally approved"));
    }

    // Final Rejection by HR:
    @PutMapping("reject-request")
    public ResponseEntity<?> rejectRequest(@RequestParam Long requestId){
        leaveService.rejectLeaveRequest(requestId);
        return
                ResponseEntity
                        .status(200)
                        .body(Map.of("message","Leave request has been finally rejected"));
    }

    @GetMapping("subordinates/get_all_requests")
    public ResponseEntity<?> getSubordinatesRequests(@RequestParam String email){
        return
                ResponseEntity.status(200)
                        .body(leaveService.getAllSubordinatesRequests(email));
    }
}
