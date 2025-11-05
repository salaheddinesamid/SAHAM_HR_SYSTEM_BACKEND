package com.saham.hr_system.controller;

import com.saham.hr_system.dto.LeaveRequestDto;
import com.saham.hr_system.service.implementation.LeaveServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // Manager approval
    @PutMapping("approve-subordinate-request")
    public ResponseEntity<?> approveSubordinateRequest(@RequestParam Long leaveRequestId) {
        return
                ResponseEntity
                        .status(200)
                        .body(Map.of("message","Leave request has been approved"));
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

    @GetMapping("get/subordinates")
    public ResponseEntity<?> getSubordinatesRequests(@RequestParam String email){
        return
                ResponseEntity.status(200)
                        .body(leaveService.getAllSubordinatesRequests(email));
    }
}
