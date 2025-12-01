package com.saham.hr_system.modules.absence.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * This interface defines methods for uploading and downloading documents related to absence requests.
 */
public interface DocumentStorageService {
    /**
     *
     * @return
     */
    String upload(String fullName ,MultipartFile file) throws IOException;

    /**
     *
     * @return
     */
    Resource download(String filePath) throws IOException;
}
