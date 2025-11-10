package com.edugate.edugateapi.controller;

import com.edugate.edugateapi.dto.course.CourseResponse;
import com.edugate.edugateapi.model.User;
import com.edugate.edugateapi.service.CourseService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
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
public class InstructorController {

    private final CourseService courseService;

    /**
     * Endpoint for the "Add Course" form.
     * It accepts 'multipart/form-data' which includes the file and text fields.
     */
    @PostMapping(value = "/courses", consumes = "multipart/form-data")
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
    public ResponseEntity<List<CourseResponse>> getMyCourses(
            @AuthenticationPrincipal User instructorUser
    ) {
        List<CourseResponse> courses = courseService.getMyCourses(instructorUser);
        return ResponseEntity.ok(courses);
    }
}