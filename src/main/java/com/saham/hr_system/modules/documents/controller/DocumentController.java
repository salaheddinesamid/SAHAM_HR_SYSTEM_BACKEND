package com.saham.hr_system.modules.documents.controller;

import com.saham.hr_system.modules.documents.dto.DocumentRequestDto;
import com.saham.hr_system.modules.documents.service.implementation.DocumentRequestApprovalImpl;
import com.saham.hr_system.modules.documents.service.implementation.DocumentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/documents")
public class DocumentController {

    private final DocumentServiceImpl documentService;
    private final DocumentRequestApprovalImpl documentRequestApproval;

    @Autowired
    public DocumentController(DocumentServiceImpl documentService, DocumentRequestApprovalImpl documentRequestApproval) {
        this.documentService = documentService;
        this.documentRequestApproval = documentRequestApproval;
    }

    @PostMapping("request")
    public ResponseEntity<?> requestDocument(@RequestParam String email, @RequestBody DocumentRequestDto requestDto) {
        documentService.requestDocument(email, requestDto);
        return ResponseEntity
                .status(200)
                .body("Document request submitted successfully");
    }

    @GetMapping("get_requests")
    public ResponseEntity<?> getDocumentRequests(@RequestParam String email) {
        return ResponseEntity
                .status(200)
                .body(documentService.getAllDocumentRequests(email));
    }

    @GetMapping("employees/get-all")
    public ResponseEntity<?> getEmployeesDocumentRequests(){
        return ResponseEntity
                .status(200)
                .body(documentService.getAllEmployeesRequests());
    }

    @PutMapping("/approve-request")
    public ResponseEntity<?> approveDocumentRequest(@RequestParam Long requestId) {
        documentRequestApproval.approveDocumentRequest(requestId);
        return ResponseEntity
                .status(200)
                .body("Document request approved successfully");
    }

    @PutMapping("/reject-request")
    public ResponseEntity<?> rejectDocumentRequest(@RequestParam Long requestId) {
        documentRequestApproval.rejectDocumentRequest(requestId);
        return ResponseEntity
                .status(200)
                .body("Document request rejected successfully");
    }
}
