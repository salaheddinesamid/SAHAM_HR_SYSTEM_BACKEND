package com.saham.hr_system.modules.documents.controller;

import com.saham.hr_system.modules.documents.dto.DocumentRequestDto;
import com.saham.hr_system.modules.documents.service.implementation.DocumentRequestApprovalImpl;
import com.saham.hr_system.modules.documents.service.implementation.DocumentRequestQueryImpl;
import com.saham.hr_system.modules.documents.service.implementation.DocumentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/documents")
public class DocumentController {

    private final DocumentServiceImpl documentService;
    private final DocumentRequestApprovalImpl documentRequestApproval;
    private final DocumentRequestQueryImpl documentRequestQuery;

    @Autowired
    public DocumentController(DocumentServiceImpl documentService, DocumentRequestApprovalImpl documentRequestApproval, DocumentRequestQueryImpl documentRequestQuery) {
        this.documentService = documentService;
        this.documentRequestApproval = documentRequestApproval;
        this.documentRequestQuery = documentRequestQuery;
    }

    @PostMapping("request")
    public ResponseEntity<?> requestDocument(@RequestParam String email, @RequestBody DocumentRequestDto requestDto) {
        documentService.requestDocument(email, requestDto);
        return ResponseEntity
                .status(200)
                .body("Document request submitted successfully");
    }

    @GetMapping("get_requests")
    public ResponseEntity<?> getDocumentRequests(
            @RequestParam String email,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize
    ) {
        return ResponseEntity
                .status(200)
                .body(
                        documentRequestQuery.getAllDocumentRequests(email, pageNumber, pageSize)
                );
    }

    @GetMapping("employees/get-all")
    public ResponseEntity<?> getEmployeesDocumentRequests(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize
    ){
        return ResponseEntity
                .status(200)
                .body(documentRequestQuery.getAllEmployeesRequests(pageNumber, pageSize));
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
