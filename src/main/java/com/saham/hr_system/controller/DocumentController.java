package com.saham.hr_system.controller;

import com.saham.hr_system.dto.DocumentRequestDto;
import com.saham.hr_system.service.implementation.DocumentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/documents")
public class DocumentController {

    private final DocumentServiceImpl documentService;

    @Autowired
    public DocumentController(DocumentServiceImpl documentService) {
        this.documentService = documentService;
    }

    @PostMapping("request")
    public ResponseEntity<?> requestDocument(String email, DocumentRequestDto requestDto) {
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
}
