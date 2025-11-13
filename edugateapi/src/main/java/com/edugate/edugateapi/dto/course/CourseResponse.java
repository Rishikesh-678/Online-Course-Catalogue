package com.edugate.edugateapi.dto.course;

import com.edugate.edugateapi.model.Course;
import com.edugate.edugateapi.model.CourseStatus;
import lombok.Builder;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@Schema(description = "Course representation returned by the API")
public class CourseResponse {
    @Schema(description = "Course id", example = "10")
    private Long id;

    @Schema(description = "Human-readable course title", example = "Spring Boot Fundamentals")
    private String courseName;

    @Schema(description = "Instructor name", example = "Dr. Jane Smith")
    private String instructor;

    @Schema(description = "Course category", example = "Backend Development")
    private String category;

    @Schema(description = "URL to the course video (e.g., YouTube)", example = "https://youtube.com/watch?v=abc123")
    private String videoLink;

    @Schema(description = "Public URL to the thumbnail image", example = "http://localhost:8080/api/images/3d2fd838-...avif")
    private String thumbnailUrl; // We will build the full URL

    @Schema(description = "Current course status", example = "LIVE")
    private CourseStatus status;

    @Schema(description = "ID of the user who created the course", example = "2")
    private Long createdById;

    @Schema(description = "Email of the course creator", example = "instructor@example.com")
    private String creatorEmail;

    /**
     * Converts a Course @Entity into a DTO.
     * @param course The entity from the database
     * @param baseUrl The base URL of the server (e.g., "http://localhost:8080")
     * @return A CourseResponse DTO
     */
    public static CourseResponse fromEntity(Course course, String baseUrl) {
        String thumbnailUrl = null;
        if (course.getThumbnail() != null && !course.getThumbnail().isEmpty()) {
            // Build the full URL to the image
            thumbnailUrl = baseUrl + "/api/images/" + course.getThumbnail();
        }

        return CourseResponse.builder()
                .id(course.getId())
                .courseName(course.getCourseName())
                .instructor(course.getInstructor())
                .category(course.getCategory())
                .videoLink(course.getVideoLink())
                .thumbnailUrl(thumbnailUrl)
                .status(course.getStatus())
                .createdById(course.getCreatedBy().getId())
                .creatorEmail(course.getCreatedBy().getEmail())
                .build();
    }
}