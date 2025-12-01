package com.saham.hr_system.modules.absence.service.implementation;

import com.saham.hr_system.modules.absence.service.DocumentStorageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class SicknessAbsenceDocumentStorageService implements DocumentStorageService {
    private final Path uploadPath;
    private static List<String> allowedExtensions = List.of(".pdf", ".jpg", ".jpeg", ".png");

    public SicknessAbsenceDocumentStorageService(@Value("${file.upload.medical-certificates}") String path) {
        this.uploadPath = Paths.get(path).toAbsolutePath().normalize();
    }

    /**
     * This method implements the upload of medical certificates for sickness absence requests.
     * @param file
     * @return
     */
    @Override
    public String upload(String fullName, MultipartFile file) throws IOException {
        // Validate if the file is empty:
        if(file.isEmpty()) {
            throw new IOException("File is empty");
        }
        // Validate the type of the file:
        String originalFilename = file.getOriginalFilename();
        String extension =
                Optional.ofNullable(
                        originalFilename
                ).filter(f-> f.contains("."))
                        .map(f-> f.substring(originalFilename.lastIndexOf(".")))
                        .orElse("")
                        .toLowerCase();

        if(!allowedExtensions.contains(extension)) {
            throw new IOException("Invalid file type. Allowed types are: " + String.join(", ", allowedExtensions));
        }

        // clean the original filename:
        assert originalFilename != null;
        String cleanFileName = StringUtils.cleanPath(originalFilename);
        if(cleanFileName.contains("..")) {
            throw new IOException("File contains invalid path sequence: " + cleanFileName);
        }

        // Create user folder:
        Path employeeFolder = uploadPath.resolve(String.valueOf(fullName));
        if(!Files.exists(employeeFolder)) {
            Files.createDirectories(employeeFolder);
        }

        String uniqueFileName = UUID.randomUUID().toString() + "_" + cleanFileName;
        // Target the path:
        Path targetPath = employeeFolder.resolve(uniqueFileName);

        // save copy of the file:
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        // Return the final relative path to save in the db:
        return fullName + "/" + uniqueFileName;

    }

    @Override
    public File download(String filePath) throws IOException {
        // resolve the file:
        Path targetPath = uploadPath.resolve(filePath).normalize();
        // converting the file path to file:
        return targetPath.toFile();
    }
}
