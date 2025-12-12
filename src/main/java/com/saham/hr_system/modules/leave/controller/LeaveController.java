package com.saham.hr_system.modules.leave.controller;

import com.saham.hr_system.jwt.JwtUtilities;
import com.saham.hr_system.modules.leave.dto.LeaveRequestDto;
import com.saham.hr_system.modules.leave.dto.LeaveRequestResponse;
import com.saham.hr_system.modules.leave.service.implementation.LeaveDocumentStorageServiceImpl;
import com.saham.hr_system.modules.leave.service.implementation.LeaveServiceImpl;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    private final JwtUtilities jwtUtilities;

    public LeaveController(LeaveServiceImpl leaveService, LeaveDocumentStorageServiceImpl documentService, JwtUtilities jwtUtilities) {
        this.leaveService = leaveService;
        this.documentService = documentService;
        this.jwtUtilities = jwtUtilities;
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

    @GetMapping("employee-leaves/get_all")
    public ResponseEntity<?> getEmployeeLeaves(@RequestParam String email){
        return ResponseEntity
                .status(200)
                .body(leaveService.getAllMyLeaves(email));
    }

    /**
     * This endpoint allows an employee to cancel a leave request by its ID.
     * @param refNumber
     * @return
     */
    @PutMapping("cancel-request")
    public ResponseEntity<?> cancelLeaveRequest(
            @RequestParam String refNumber
    ){
       leaveService.cancelRequest(refNumber);
       return ResponseEntity
               .status(200)
               .body(Map.of("message","Leave request has been cancelled successfully"));
    }

    /**
     * This endpoint allows an employee to cancel a leave by Ref N°.
     * @param refNumber
     * @return
     */
    @DeleteMapping("cancel")
    public ResponseEntity<?> cancelLeave(String refNumber){
        leaveService.cancelLeave(refNumber);

        return ResponseEntity.status(200)
                .body(Map.of("message","Leave has been cancelled successfully"));
    }
    /**
     * Get the leave requests of an employee
     * @param email
     * @return
     */
    @GetMapping("/requests/get")
    public ResponseEntity<?> getLeaves(
            @RequestParam String email,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "2") int pageSize
    ){
        return ResponseEntity
                .status(200)
                .body(leaveService.getAllLeaveRequests(email, pageNumber, pageSize));
    }
    /**
     *
     * @return
     */
    @GetMapping("/requests/hr/get_all")
    public ResponseEntity<?> getAllRequestsForHR(
            @RequestParam(defaultValue = "0" ) int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize
    ){
        List<LeaveRequestResponse> requests = leaveService.getAllLeaveRequestsForHR(
                pageNumber,
                pageSize
        );

        return ResponseEntity.status(200)
                .body(requests);
    }
    /**
     *
     * @param leaveRequestId
     * @return
     */
    @PutMapping("/requests/hr/approve")
    public ResponseEntity<?> approveRequest(@RequestParam Long leaveRequestId) {
        leaveService.approveLeaveRequest(leaveRequestId);
        return
                ResponseEntity
                        .status(200)
                        .body(Map.of("message","Leave request has been finally approved"));
    }

    /**
     *
     * @param requestId
     * @return
     */
    @PutMapping("/requests/hr/reject")
    public ResponseEntity<?> rejectRequest(@RequestParam Long requestId){
        leaveService.rejectLeaveRequest(requestId);
        return
                ResponseEntity
                        .status(200)
                        .body(Map.of("message","Leave request has been finally rejected"));
    }

    /**
     * This endpoint handles the approval of a subordinate's leave request by a manager.
     * This operation requires fine-grained access to ensure that only authorized managers can approve their subordinates' requests.
     * @param authentication
     * @param leaveRequestId
     * @return
     */
    @PutMapping("/requests/subordinates/approve-request")
    public ResponseEntity<?> approveSubordinateRequest(Authentication authentication,@RequestParam Long leaveRequestId) {
        String approvedBy = authentication.getName();  // extract the email from the authentication object
        leaveService.approveSubordinateLeaveRequest(approvedBy,leaveRequestId);
        return
                ResponseEntity
                        .status(200)
                        .body(Map.of("message","Leave request has been approved"));
    }

    /**
     *
     * @param authentication
     * @param leaveRequestId
     * @return
     */
    @PutMapping("/requests/subordinates/reject-request")
    public ResponseEntity<?> rejectSubordinateRequest(Authentication authentication,@RequestParam Long leaveRequestId) {
        String rejectedBy = authentication.getName();
        leaveService.rejectSubordinateLeaveRequest(rejectedBy, leaveRequestId);
        return
                ResponseEntity
                        .status(200)
                        .body(Map.of("message","Leave request has been rejected"));
    }
    /**
     * This endpoint handles the retrieval of all leave requests made by subordinates of a specific manager.
     * This endpoint requires fine-grained access to ensure that only authorized managers can view their subordinates' requests.
     * @param email
     * @return
     */
    @GetMapping("/requests/subordinates/get_all")
    public ResponseEntity<?> getSubordinatesRequests(@RequestParam String email,
                                                     @RequestParam(defaultValue = "0") int pageNumber,
                                                     @RequestParam(defaultValue = "5") int pageSize
                                                     ){
        //String managerEmail = authentication.getName();
        return
                ResponseEntity.status(200)
                        .body(leaveService.getAllSubordinatesRequests(email, pageNumber, pageSize));
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
