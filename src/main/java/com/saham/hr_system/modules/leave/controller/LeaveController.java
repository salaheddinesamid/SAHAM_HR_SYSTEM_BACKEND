package com.saham.hr_system.modules.leave.controller;

import com.saham.hr_system.modules.leave.dto.LeaveRequestDto;
import com.saham.hr_system.modules.leave.dto.LeaveRequestResponse;
import com.saham.hr_system.modules.leave.service.implementation.LeaveDocumentStorageServiceImpl;
import com.saham.hr_system.modules.leave.service.implementation.LeaveServiceImpl;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/leaves")
public class LeaveController {

    private final LeaveServiceImpl leaveService;
    private final LeaveDocumentStorageServiceImpl documentService;

    public LeaveController(LeaveServiceImpl leaveService, LeaveDocumentStorageServiceImpl documentService) {
        this.leaveService = leaveService;
        this.documentService = documentService;
    }

    /*
    @PostMapping("apply")
    public ResponseEntity<?> applyForLeave(@RequestParam String email,
                                           @RequestBody LeaveRequestDto leaveRequestDto
                                           ){
        leaveService.requestLeave(email,leaveRequestDto);
        return ResponseEntity
                .status(200)
                .body("Leave applied successfully");
    }


     */
    @PostMapping("apply")
    public ResponseEntity<?> applyForLeave(@RequestParam String email,
                                           @RequestPart("requestDto") LeaveRequestDto requestDto,
                                           @RequestPart(value = "file", required = false) MultipartFile file
    ) throws IOException, MessagingException {
        leaveService.requestLeave(email,requestDto, file);
        return ResponseEntity
                .status(200)
                .body("Leave applied successfully");
    }

    @DeleteMapping("cancel")
    public ResponseEntity<?> cancelLeaveRequest(
            @RequestParam Long leaveRequestId
    ){
       leaveService.cancelRequest(leaveRequestId);
       return ResponseEntity
               .status(200)
               .body(Map.of("message","Leave request has been cancelled successfully"));
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
    public ResponseEntity<?> approveSubordinateRequest(@RequestParam String approvedBy,@RequestParam Long leaveRequestId) {
        leaveService.approveSubordinateLeaveRequest(approvedBy,leaveRequestId);
        return
                ResponseEntity
                        .status(200)
                        .body(Map.of("message","Leave request has been approved"));
    }

    // Manager rejection
    @PutMapping("subordinates/reject-request")
    public ResponseEntity<?> rejectSubordinateRequest(@RequestParam String rejectedBy,@RequestParam Long leaveRequestId) {
        leaveService.rejectSubordinateLeaveRequest(rejectedBy, leaveRequestId);
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

    // Upload medical certificates:
    @PostMapping("/medical-certificate/upload")
    public ResponseEntity<?> handleUpload(@RequestParam("file") MultipartFile file) throws IOException {
        try{
            String fileName = documentService.upload(20L,file);
            return ResponseEntity.status(200).body(fileName);
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }

    }
}
