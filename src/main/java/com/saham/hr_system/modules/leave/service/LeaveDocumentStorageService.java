package com.saham.hr_system.modules.leave.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

public interface LeaveDocumentStorageService {

    /**
     * This function handles document uploading and storage
     * @param file
     */
    String upload(Long employeeId,MultipartFile file) throws IOException;

    /**
     *
     */
    Resource download(String path) throws MalformedURLException;

    /**
     *
     */
    void loadAll();
}
