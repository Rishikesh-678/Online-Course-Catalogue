package com.edugate.edugateapi.service;

import com.edugate.edugateapi.dto.course.CourseResponse;
import com.edugate.edugateapi.exception.BadRequestException;
import com.edugate.edugateapi.exception.ResourceNotFoundException;
import com.edugate.edugateapi.model.Course;
import com.edugate.edugateapi.model.CourseStatus;
import com.edugate.edugateapi.model.User;
import com.edugate.edugateapi.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final FileStorageService fileStorageService;

    // Helper to get the base URL (e.g., http://localhost:8080)
    private String getBaseUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    }

    /**
     * Creates a new course and sets it to PENDING_ADDITION.
     */
    @Transactional
    public CourseResponse createCourse(String courseName, String instructorName, String category,
                                     String videoLink, MultipartFile thumbnailFile, User instructor) {
        
        // 1. Store the file and get its unique name
        String thumbnailFilename = fileStorageService.store(thumbnailFile);

        // 2. Create the new Course entity
        Course course = Course.builder()
                .courseName(courseName)
                .instructor(instructorName)
                .category(category)
                .videoLink(videoLink)
                .thumbnail(thumbnailFilename) // Save just the filename
                .createdBy(instructor)        // Set ownership
                .status(CourseStatus.PENDING_ADDITION) // Set initial status
                .build();
        
        // 3. Save to database
        Course savedCourse = courseRepository.save(course);

        return CourseResponse.fromEntity(savedCourse, getBaseUrl());
    }

    /**
     * Allows an instructor to request removal of their own course.
     */
    @Transactional
    public void requestCourseRemoval(Long courseId, User instructor) {
        Course course = findCourseById(courseId);
        checkOwnership(course, instructor); // Verify this instructor owns this course

        if (course.getStatus().equals(CourseStatus.APPROVED)) {
            // If live, set to pending removal for admin approval
            course.setStatus(CourseStatus.PENDING_REMOVAL);
            courseRepository.save(course);
        } else if (course.getStatus().equals(CourseStatus.PENDING_ADDITION)) {
            // If it was never approved, just delete it immediately
            courseRepository.delete(course);
        } else {
            throw new BadRequestException("This course is already pending removal or cannot be removed.");
        }
    }

    /**
     * Gets all courses created by the currently logged-in instructor.
     */
    public List<CourseResponse> getMyCourses(User instructor) {
        // We will add this new method to the CourseRepository
        return courseRepository.findByCreatedById(instructor.getId()).stream()
                .map(course -> CourseResponse.fromEntity(course, getBaseUrl()))
                .collect(Collectors.toList());
    }

    // --- Helper Methods ---

    private Course findCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));
    }

    private void checkOwnership(Course course, User instructor) {
        if (!course.getCreatedBy().getId().equals(instructor.getId())) {
            throw new AccessDeniedException("You do not have permission to modify this course.");
        }
    }
}