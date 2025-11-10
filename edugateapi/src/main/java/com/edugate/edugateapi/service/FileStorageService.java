package com.edugate.edugateapi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.springframework.util.StringUtils;

@Service
public class FileStorageService {

    private final Path rootLocation;

    // We get the file.upload-dir from application.properties
    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) {
        this.rootLocation = Paths.get(uploadDir);
        try {
            // Ensure the upload directory exists
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage location", e);
        }
    }

    /**
     * Saves an uploaded file.
     * @param file The file from the request
     * @return The unique, generated filename
     */
    public String store(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("Failed to store empty file.");
        }

        // Generate a unique filename (e.g., "123e4567-e89b-12d3-a456-426614174000.png")
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String uniqueFilename = UUID.randomUUID().toString() + extension;

        try (InputStream inputStream = file.getInputStream()) {
            Path destinationFile = this.rootLocation.resolve(Paths.get(uniqueFilename))
                    .normalize().toAbsolutePath();

            // Security check
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                throw new RuntimeException("Cannot store file outside current directory.");
            }

            // Save the file
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            
            return uniqueFilename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file.", e);
        }
    }
}