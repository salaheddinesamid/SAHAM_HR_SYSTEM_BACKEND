package com.saham.hr_system.modules.absence.controller;

import com.saham.hr_system.modules.absence.dto.AbsenceRequestDetails;
import com.saham.hr_system.modules.absence.dto.AbsenceRequestDto;
import com.saham.hr_system.modules.absence.service.implementation.AbsenceRequestQueryImpl;
import com.saham.hr_system.modules.absence.service.implementation.AbsenceRequestServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/absences")
public class AbsenceController {

    private final AbsenceRequestServiceImpl absenceRequestService;
    private final AbsenceRequestQueryImpl absenceRequestQuery;

    public AbsenceController(AbsenceRequestServiceImpl absenceRequestService, AbsenceRequestQueryImpl absenceRequestQuery) {
        this.absenceRequestService = absenceRequestService;
        this.absenceRequestQuery = absenceRequestQuery;
    }

    @PostMapping(value = "/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> newAbsence(@RequestParam String email,
                                        @ModelAttribute AbsenceRequestDto requestDto) throws Exception {
        // Act:
        AbsenceRequestDetails response = absenceRequestService.requestAbsence(email,requestDto);
        // return HTTP response:
        return ResponseEntity
                .status(200)
                .body(response);
    }

    @GetMapping("/employee-absences/get_all")
    public ResponseEntity<?> getAllEmployeeAbsences(
            @RequestParam String email
    ) {
        List<AbsenceRequestDetails> response = absenceRequestQuery.getAllMyAbsenceRequests(email);
        return ResponseEntity
                .status(200)
                .body(response);
    }

    @GetMapping("/subordinates-absences/get_all")
    public ResponseEntity<?> getAllSubordinatesAbsences(
            @RequestParam String email
    ){
        List<AbsenceRequestDetails> response = absenceRequestQuery.getAllSubordinateAbsenceRequests(email);
        return ResponseEntity
                .status(200)
                .body(response);
    }
}
