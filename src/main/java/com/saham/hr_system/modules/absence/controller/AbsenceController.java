package com.saham.hr_system.modules.absence.controller;

import com.saham.hr_system.modules.absence.dto.AbsenceRequestDetails;
import com.saham.hr_system.modules.absence.dto.AbsenceRequestDto;
import com.saham.hr_system.modules.absence.service.implementation.AbsenceRequestQueryImpl;
import com.saham.hr_system.modules.absence.service.implementation.AbsenceRequestServiceImpl;
import com.saham.hr_system.modules.absence.service.implementation.SicknessAbsenceDocumentStorageService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/absences")
public class AbsenceController {

    private final AbsenceRequestServiceImpl absenceRequestService;
    private final AbsenceRequestQueryImpl absenceRequestQuery;
    private final SicknessAbsenceDocumentStorageService sicknessAbsenceDocumentStorageService;

    public AbsenceController(AbsenceRequestServiceImpl absenceRequestService, AbsenceRequestQueryImpl absenceRequestQuery, SicknessAbsenceDocumentStorageService sicknessAbsenceDocumentStorageService) {
        this.absenceRequestService = absenceRequestService;
        this.absenceRequestQuery = absenceRequestQuery;
        this.sicknessAbsenceDocumentStorageService = sicknessAbsenceDocumentStorageService;
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

    /*
    @GetMapping("/subordinates-absences/get_all")
    public ResponseEntity<?> getAllSubordinatesAbsences(
            @RequestParam String email
    ){
        List<AbsenceRequestDetails> response = absenceRequestQuery.getAllSubordinateAbsenceRequests(email);
        return ResponseEntity
                .status(200)
                .body(response);
    }

     */

    @GetMapping("/requests/subordinates/get_all")
    public ResponseEntity<?> getAllRequestsForManager(@RequestParam String email) {
        List<AbsenceRequestDetails> response = absenceRequestQuery.getAllSubordinateAbsenceRequests(
                email
        );

        return ResponseEntity.status(200)
                .body(response);
    }

    @GetMapping("/requests/hr/get_all")
    public ResponseEntity<?> getAllRequestsForHR(){
        List<AbsenceRequestDetails> response =
                absenceRequestQuery.getAllForHR();

        return ResponseEntity.status(200)
                .body(response);
    }

    @PutMapping("/requests/subordinates/approve-request")
    public ResponseEntity<?> approveSubordinateRequest(
            @RequestParam String approvedBy,
            @RequestParam String refNumber
    ) throws Exception {
        absenceRequestService.approveAbsenceRequest(approvedBy,refNumber);
        return
                ResponseEntity
                        .status(200)
                        .body("Absence request has been approved");
    }

    @PutMapping("/requests/approve")
    public ResponseEntity<?> approveAbsenceRequest(
            @RequestParam String refNumber
    ){

        return null;
    }

    @GetMapping("/medical-certificates/download")
    public ResponseEntity<Resource> downloadMedicalCertificate(
            @RequestParam String path
    ) throws IOException {

        File file = sicknessAbsenceDocumentStorageService.download(path);
        // create HTTP headers:
        HttpHeaders headers  = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"");

        // Return input stream resource
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
