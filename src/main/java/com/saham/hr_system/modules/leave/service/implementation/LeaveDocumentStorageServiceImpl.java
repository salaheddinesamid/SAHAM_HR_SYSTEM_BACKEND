package com.saham.hr_system.modules.leave.service.implementation;

import com.saham.hr_system.modules.leave.service.LeaveDocumentStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class LeaveDocumentStorageServiceImpl implements LeaveDocumentStorageService {

    private final Path uploadPath;

    public LeaveDocumentStorageServiceImpl(@Value("${file.upload.medical-certificates}") String savePath) {
        this.uploadPath = Paths.get(savePath).toAbsolutePath().normalize();
    }

    /**
     * This implementation handles file upload and storage
     * @param file
     */
    @Override
    public String upload(Long employeeId, MultipartFile file) throws IOException {

        // 1. VALIDATE EMPTY FILE
        if (file.isEmpty()) {
            throw new IOException("Uploaded file is empty");
        }

        // 2. VALIDATE FILE TYPE
        String originalName = file.getOriginalFilename();
        String extension = Optional.ofNullable(originalName)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(originalName.lastIndexOf(".")))
                .orElse("")
                .toLowerCase();

        List<String> allowedExtensions = List.of(".pdf", ".jpg", ".jpeg", ".png");

        if (!allowedExtensions.contains(extension)) {
            throw new IOException("Invalid file type. Allowed: PDF, JPG, PNG");
        }

        // 3. CLEAN ORIGINAL NAME
        String cleanName = StringUtils.cleanPath(Objects.requireNonNull(originalName));

        if (cleanName.contains("..")) {
            throw new IOException("Invalid filename: " + cleanName);
        }

        // 4. CREATE USER FOLDER: /uploads/employeeId/
        Path employeeFolder = uploadPath.resolve(String.valueOf(employeeId));
        if (!Files.exists(employeeFolder)) {
            Files.createDirectories(employeeFolder);
        }

        // 5. GENERATE UNIQUE FILE NAME
        String uniqueName = UUID.randomUUID().toString() + "_" + cleanName;

        // 6. TARGET PATH
        Path target = employeeFolder.resolve(uniqueName);

        // 7. SAVE FILE
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        // 8. RETURN RELATIVE PATH
        return employeeId + "/" + uniqueName;
    }


    @Override
    public Resource download(String path) {
        return null;
    }

    @Override
    public void loadAll() {

    }
}
