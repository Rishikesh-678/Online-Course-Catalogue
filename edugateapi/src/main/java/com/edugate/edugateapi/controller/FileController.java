package com.edugate.edugateapi.controller;

import org.springframework.beans.factory.annotation.Value;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/images") // This path is public
@Tag(name = "File Management", description = "Endpoints for serving uploaded files and media content")
public class FileController {

    private final Path rootLocation;

    public FileController(@Value("${file.upload-dir}") String uploadDir) {
        this.rootLocation = Paths.get(uploadDir);
    }

    @GetMapping("/{filename:.+}")
    @Operation(summary = "Retrieve image file", description = "Serves image files (thumbnails, course images, etc.) from the upload directory")
    @ApiResponse(responseCode = "200", description = "File retrieved successfully", content = @Content(mediaType = "image/*"))
    @ApiResponse(responseCode = "404", description = "File not found", content = @Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":false,\"status\":404,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/images/{filename}\",\"message\":\"File not found\",\"data\":null}")))
    @ApiResponse(responseCode = "400", description = "Invalid file path or malformed URL", content = @Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":false,\"status\":400,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/images/{filename}\",\"message\":\"Invalid file path or malformed URL\",\"data\":null}")))
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                // Return the image
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .contentType(MediaType.IMAGE_PNG) // Assuming all icons are PNGs
                        .body(resource);
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}