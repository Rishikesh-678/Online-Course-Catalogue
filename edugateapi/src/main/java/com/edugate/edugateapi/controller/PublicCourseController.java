package com.edugate.edugateapi.controller;

import com.edugate.edugateapi.dto.course.CourseResponse;
import com.edugate.edugateapi.service.UserService;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Public endpoints for browsing courses without authentication.
 * These endpoints are accessible to anonymous users.
 */
@RestController
@RequestMapping("/api/public/courses")
@RequiredArgsConstructor
@Tag(name = "Public Courses", description = "Public endpoints for browsing live courses without authentication")
public class PublicCourseController {

    private final UserService userService;

    /**
     * Gets all live/published courses (public, no auth required).
     */
    @GetMapping
    @Operation(summary = "Get all live courses", description = "Retrieve all courses that are currently live/published - no authentication required")
    @ApiResponse(responseCode = "200", description = "List of live courses", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class)))
    public ResponseEntity<List<CourseResponse>> getAllLiveCourses() {
        return ResponseEntity.ok(userService.getAllLiveCourses());
    }

    /**
     * Gets a single live/published course by ID (public, no auth required).
     */
    @GetMapping("/{courseId}")
    @Operation(summary = "Get a single live course", description = "Retrieve a specific course by ID if it is live/published - no authentication required")
    @ApiResponse(responseCode = "200", description = "Course details", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class)))
    @ApiResponse(responseCode = "404", description = "Course not found", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class)))
    public ResponseEntity<CourseResponse> getLiveCourseById(
            @PathVariable Long courseId
    ) {
        return ResponseEntity.ok(userService.getLiveCourseById(courseId));
    }
}
