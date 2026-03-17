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
/** * This component is responsible for handling the upload of employee profile pictures.
 * It takes care of saving the uploaded file to a specified directory and returns the filename.
 */
@Component
public class EmployeeProfilePictureUploader {

    private final Path uploadPath;

    public EmployeeProfilePictureUploader(@Value("${file.upload.profile-pictures}") String path) {
        this.uploadPath = Paths.get(path).toAbsolutePath().normalize();
    }
    /**
     * Uploads a profile picture for an employee.
     * @param multipartFile The file to be uploaded.
     * @param matriculation The matriculation number of the employee, used to generate the filename.
     * @return The filename of the uploaded profile picture.
     */
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
