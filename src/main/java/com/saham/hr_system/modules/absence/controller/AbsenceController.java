package com.saham.hr_system.modules.absence.controller;

import com.saham.hr_system.modules.absence.dto.AbsenceRequestDetails;
import com.saham.hr_system.modules.absence.dto.AbsenceRequestDto;
import com.saham.hr_system.modules.absence.service.implementation.AbsenceRejectionImpl;
import com.saham.hr_system.modules.absence.service.implementation.AbsenceRequestQueryImpl;
import com.saham.hr_system.modules.absence.service.implementation.AbsenceRequestServiceImpl;
import com.saham.hr_system.modules.absence.service.implementation.SicknessAbsenceDocumentStorageService;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 * REST controller responsible for managing absence requests in the HR system.
 * <p>
 * This controller exposes endpoints for:
 * <ul>
 *     <li>Submitting new absence requests</li>
 *     <li>Fetching employee and subordinate absence requests</li>
 *     <li>Approving or rejecting requests by Managers and HR</li>
 *     <li>Downloading medical certificates associated with sickness absence</li>
 * </ul>
 *
 * All routes are versioned under <b>/api/v1/absences</b>.
 */
@RestController
@RequestMapping("/api/v1/absences")
public class AbsenceController {

    private final AbsenceRequestServiceImpl absenceRequestService;
    private final AbsenceRequestQueryImpl absenceRequestQuery;
    private final AbsenceRejectionImpl absenceRejection;
    private final SicknessAbsenceDocumentStorageService sicknessAbsenceDocumentStorageService;

    /**
     * Constructs the {@link AbsenceController} with all required service dependencies.
     *
     * @param absenceRequestService             service handling creation and approval of absence requests
     * @param absenceRequestQuery               service handling querying of absence requests
     * @param absenceRejection                  service handling rejection logic
     * @param sicknessAbsenceDocumentStorageService service for retrieving stored medical certificates
     */
    public AbsenceController(
            AbsenceRequestServiceImpl absenceRequestService,
            AbsenceRequestQueryImpl absenceRequestQuery,
            AbsenceRejectionImpl absenceRejection,
            SicknessAbsenceDocumentStorageService sicknessAbsenceDocumentStorageService
    ) {
        this.absenceRequestService = absenceRequestService;
        this.absenceRequestQuery = absenceRequestQuery;
        this.absenceRejection = absenceRejection;
        this.sicknessAbsenceDocumentStorageService = sicknessAbsenceDocumentStorageService;
    }

    /**
     * Creates a new absence request for the provided employee.
     *
     * @param email       the email of the employee submitting the request
     * @param requestDto  the request payload containing absence details and optional documents
     * @return HTTP 200 with {@link AbsenceRequestDetails} describing the newly created request
     * @throws Exception if validation fails or service processing throws an error
     */
    @PostMapping(value = "/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> newAbsence(
            @RequestParam String email,
            @ModelAttribute AbsenceRequestDto requestDto
    ) throws Exception {
        AbsenceRequestDetails response = absenceRequestService.requestAbsence(email, requestDto);
        return ResponseEntity.status(200).body(response);
    }

    /**
     * Retrieves all absence requests submitted by a specific employee.
     *
     * @param email the employee's email
     * @return a list of {@link AbsenceRequestDetails} representing the employee’s absence history
     */
    @GetMapping("/employee-absences/get_all")
    public ResponseEntity<?> getAllEmployeeAbsences(@RequestParam String email,
                                                    @RequestParam(defaultValue = "0") int pageNumber,
                                                    @RequestParam(defaultValue = "5") int pageSize
    ) {
        Page<AbsenceRequestDetails> response = absenceRequestQuery.getAllMyAbsenceRequests(email, pageNumber, pageSize);
        return ResponseEntity.status(200).body(response);
    }

    /**
     * Retrieves all absence requests requiring review by a manager.
     *
     * @param email email of the manager
     * @return list of subordinate absence requests awaiting approval or rejection
     */
    @GetMapping("/requests/subordinates/get_all")
    public ResponseEntity<?> getAllRequestsForManager(@RequestParam String email,
                                                      @RequestParam(defaultValue = "0") int pageNumber,
                                                      @RequestParam(defaultValue = "5") int pageSize
    ) {
        Page<AbsenceRequestDetails> response =
                absenceRequestQuery.getAllSubordinateAbsenceRequests(email, pageNumber, pageSize);
        return ResponseEntity.status(200).body(response);
    }

    /**
     * Approves an absence request that belongs to a subordinate employee.
     *
     * @param authentication the authenticated manager (used to extract approver email)
     * @param refNumber      reference number of the absence request
     * @return HTTP success message after approval
     * @throws Exception if the request cannot be processed
     */
    @PutMapping("/requests/subordinates/approve-request")
    public ResponseEntity<?> approveSubordinateRequest(
            Authentication authentication,
            @RequestParam String refNumber
    ) throws Exception {
        String approvedBy = authentication.getName();
        absenceRequestService.approveAbsenceRequest(approvedBy, refNumber);
        return ResponseEntity.status(200).body("Absence request has been approved");
    }

    /**
     * Rejects an absence request belonging to a subordinate employee.
     *
     * @param rejectedBy email of the manager rejecting the request
     * @param refNumber  reference number of the absence request
     * @return HTTP success message after rejection
     * @throws Exception if rejection fails
     */
    @PutMapping("/requests/subordinates/reject-request")
    public ResponseEntity<?> rejectSubordinateRequest(
            @RequestParam String rejectedBy,
            @RequestParam String refNumber
    ) throws Exception {
        absenceRejection.rejectSubordinate(rejectedBy, refNumber);
        return ResponseEntity.status(200).body("Absence request has been rejected");
    }

    /**
     * Retrieves all absence requests that require HR review.
     *
     * @return list of all pending absence requests for HR processing
     */
    @GetMapping("/requests/hr/get_all")
    public ResponseEntity<?> getAllRequestsForHR(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize
    ) {
        Page<AbsenceRequestDetails> response = absenceRequestQuery.getAllForHR(pageNumber, pageSize);
        return ResponseEntity.status(200).body(response);
    }

    /**
     * Allows HR to reject an absence request.
     *
     * @param refNumber the reference number of the absence request
     * @return confirmation message
     * @throws Exception if request is not found or processing fails
     */
    @PutMapping("/requests/hr/reject")
    public ResponseEntity<?> rejectAbsenceRequest(@RequestParam String refNumber) throws Exception {
        absenceRejection.rejectAbsence(refNumber);
        return ResponseEntity.status(200).body("Absence request has been rejected");
    }

    /**
     * Allows HR to approve an absence request.
     *
     * @param refNumber the reference number of the absence request
     * @return confirmation message
     * @throws Exception if processing fails
     */
    @PutMapping("/requests/hr/approve")
    public ResponseEntity<?> approveAbsenceRequest(@RequestParam String refNumber) throws Exception {
        absenceRequestService.approveAbsence(refNumber);
        return ResponseEntity.status(200).body("Absence request has been finally approved");
    }

    /**
     * Downloads a medical certificate uploaded for a sickness absence.
     *
     * @param path the storage path of the document
     * @return a downloadable {@link Resource} representing the medical certificate file
     * @throws IOException if file retrieval fails
     */
    @GetMapping("/medical-certificates/download")
    public ResponseEntity<Resource> downloadMedicalCertificate(@RequestParam String path)
            throws IOException {

        Resource resource = sicknessAbsenceDocumentStorageService.download(path);

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        // Detect correct content type
        String contentType = Files.probeContentType(resource.getFile().toPath());
        if (contentType == null) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        // Extract real filename
        String filename = resource.getFilename();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }

}
