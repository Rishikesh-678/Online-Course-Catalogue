package com.edugate.edugateapi.controller;

import com.edugate.edugateapi.dto.course.CourseResponse;
import com.edugate.edugateapi.model.User;
import com.edugate.edugateapi.service.CourseService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/instructor")
@RequiredArgsConstructor
@PreAuthorize("hasRole('INSTRUCTOR')") // This secures all endpoints in this class
@Tag(name = "Instructor Management", description = "Endpoints for instructors to create and manage their courses")
public class InstructorController {

    private final CourseService courseService;

    /**
     * Endpoint for the "Add Course" form.
     * It accepts 'multipart/form-data' which includes the file and text fields.
     */
    @PostMapping(value = "/courses", consumes = "multipart/form-data")
    @Operation(summary = "Submit a new course", description = "Creates and submits a new course for admin approval; accepts multipart/form-data including thumbnail image")
    @ApiResponse(
        responseCode = "201",
        description = "Course submitted successfully",
        content = @Content(
            schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class),
            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":true,\"status\":201,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/instructor/courses\",\"message\":\"Course submitted successfully\",\"data\":{\"id\":101,\"courseName\":\"Spring Boot Advanced\",\"instructor\":\"Dr. Jane Smith\",\"category\":\"Backend Development\",\"videoLink\":\"https://youtube.com/watch?v=xyz\",\"thumbnailUrl\":\"http://localhost:8080/api/images/abc.avif\",\"status\":\"PENDING_ADDITION\",\"createdById\":2,\"creatorEmail\":\"instructor@example.com\"}}")
        )
    )
    @ApiResponse(responseCode = "400", description = "Bad request - validation failed or file upload error", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":false,\"status\":400,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/instructor/courses\",\"message\":\"Bad request - validation failed or file upload error\",\"data\":null}")))
    @ApiResponse(responseCode = "403", description = "Forbidden - Instructor role required", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":false,\"status\":403,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/instructor/courses\",\"message\":\"Forbidden - Instructor role required\",\"data\":null}")))
    public ResponseEntity<CourseResponse> createCourse(
            @RequestParam("courseName") @NotBlank String courseName,
            @RequestParam("instructor") @NotBlank String instructor,
            @RequestParam("category") @NotBlank String category,
            @RequestParam("videoLink") @NotBlank String videoLink,
            @RequestParam("thumbnail") MultipartFile thumbnailFile,
            @AuthenticationPrincipal User instructorUser // Get the logged-in instructor
    ) {
        CourseResponse createdCourse = courseService.createCourse(
                courseName, instructor, category, videoLink, thumbnailFile, instructorUser
        );
        return new ResponseEntity<>(createdCourse, HttpStatus.CREATED);
    }

    /**
     * Endpoint for an instructor to request removal of their own course.
     */
    @DeleteMapping("/courses/{courseId}")
    @Operation(summary = "Request course removal", description = "Request removal of a course submitted by the authenticated instructor")
    @ApiResponse(responseCode = "204", description = "Course removal requested successfully", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":true,\"status\":204,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/instructor/courses/10\",\"message\":\"Course removal requested successfully\",\"data\":null}")))
    @ApiResponse(responseCode = "404", description = "Course not found", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":false,\"status\":404,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/instructor/courses/999\",\"message\":\"Course not found\",\"data\":null}")))
    @ApiResponse(responseCode = "403", description = "Forbidden - You can only remove your own courses", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":false,\"status\":403,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/instructor/courses/10\",\"message\":\"Forbidden - You can only remove your own courses\",\"data\":null}")))
    public ResponseEntity<Void> requestCourseRemoval(
            @PathVariable Long courseId,
            @AuthenticationPrincipal User instructorUser
    ) {
        courseService.requestCourseRemoval(courseId, instructorUser);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint for an instructor to see all the courses they have submitted.
     */
    @GetMapping("/courses/my-courses")
    @Operation(summary = "Get instructor's courses", description = "Retrieve all courses submitted by the authenticated instructor, regardless of approval status")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved instructor's courses", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":true,\"status\":200,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/instructor/courses/my-courses\",\"message\":\"My courses\",\"data\":[{\"id\":10,\"courseName\":\"Advanced Spring\",\"instructor\":\"Dr. John\",\"category\":\"Backend\",\"videoLink\":\"https://youtube.com/watch?v=xyz\",\"status\":\"APPROVED\"}]}")))
    @ApiResponse(responseCode = "403", description = "Forbidden - Instructor role required", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":false,\"status\":403,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/instructor/courses/my-courses\",\"message\":\"Forbidden - Instructor role required\",\"data\":null}")))
    public ResponseEntity<List<CourseResponse>> getMyCourses(
            @AuthenticationPrincipal User instructorUser
    ) {
        List<CourseResponse> courses = courseService.getMyCourses(instructorUser);
        return ResponseEntity.ok(courses);
    }
}