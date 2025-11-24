package com.saham.hr_system.modules.absence.controller;

import com.saham.hr_system.modules.absence.dto.AbsenceRequestDetails;
import com.saham.hr_system.modules.absence.dto.AbsenceRequestDto;
import com.saham.hr_system.modules.absence.service.implementation.AbsenceRequestServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/absences")
public class AbsenceController {

    private final AbsenceRequestServiceImpl absenceRequestService;

    public AbsenceController(AbsenceRequestServiceImpl absenceRequestService) {
        this.absenceRequestService = absenceRequestService;
    }

    @PostMapping("new")
    public ResponseEntity<?> newAbsence(@RequestParam String email, @RequestBody AbsenceRequestDto requestDto) throws Exception {
        // Act:
        AbsenceRequestDetails response = absenceRequestService.requestAbsence(email,requestDto);
        // return HTTP response:
        return ResponseEntity
                .status(200)
                .body(response);
    }
}
