package com.edugate.edugateapi.dto.course;

import com.edugate.edugateapi.model.Course;
import com.edugate.edugateapi.model.CourseStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseResponse {
    private Long id;
    private String courseName;
    private String instructor;
    private String category;
    private String videoLink;
    private String thumbnailUrl; // We will build the full URL
    private CourseStatus status;
    private Long createdById;
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