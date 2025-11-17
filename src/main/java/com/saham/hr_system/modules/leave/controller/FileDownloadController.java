package com.saham.hr_system.modules.leave.controller;

import com.saham.hr_system.modules.leave.service.implementation.LeaveDocumentStorageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;

@RestController
@RequestMapping("/api/v1/files")
public class FileDownloadController {

    private final LeaveDocumentStorageServiceImpl leaveDocumentStorageService;

     @Autowired
    public FileDownloadController(LeaveDocumentStorageServiceImpl leaveDocumentStorageService) {
        this.leaveDocumentStorageService = leaveDocumentStorageService;
    }

    @GetMapping("download")
    public ResponseEntity<Resource> download(@RequestParam String filePath) throws MalformedURLException {
         Resource resource = leaveDocumentStorageService.download(filePath);
        // Force PDF MIME type
        MediaType mediaType = MediaType.APPLICATION_PDF;

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
