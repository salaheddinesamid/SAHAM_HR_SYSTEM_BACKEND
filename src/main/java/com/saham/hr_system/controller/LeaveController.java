package com.saham.hr_system.controller;

import com.saham.hr_system.dto.LeaveRequestDto;
import com.saham.hr_system.service.implementation.LeaveServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
