package com.saham.hr_system.modules.employees.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Component
public class EmployeeProfilePictureUploader {

    private final Path uploadPath;

    public EmployeeProfilePictureUploader(@Value("${file.upload.profile-pictures}") String path) {
        this.uploadPath = Paths.get(path).toAbsolutePath().normalize();
    }

    public String uploadProfilePicture(MultipartFile multipartFile, String matriculation) {
        try {

            // Ensure directory exists
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String extension = Objects
                    .requireNonNull(multipartFile.getOriginalFilename())
                    .split("\\.")[1];

            String fileName = matriculation + "." + extension;

            Path copyPath = uploadPath.resolve(fileName);

            Files.copy(
                    multipartFile.getInputStream(),
                    copyPath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return fileName;

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload profile picture", e);
        }
    }
}
