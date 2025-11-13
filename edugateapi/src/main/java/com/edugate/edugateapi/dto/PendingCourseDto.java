package com.edugate.edugateapi.dto;

import com.edugate.edugateapi.model.Course;
import com.edugate.edugateapi.model.CourseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Summary info for courses pending admin approval")
public class PendingCourseDto {
    @Schema(description = "Pending course id", example = "42")
    private Long id;

    @Schema(description = "Course title", example = "Reactive Spring Boot")
    private String courseName;

    @Schema(description = "Instructor display name", example = "Dr. Jane Smith")
    private String instructorName; // The "Taken By" field

    @Schema(description = "Email of the instructor who submitted the course", example = "instructor@example.com")
    private String creatorEmail; // The email of the instructor who submitted it

    @Schema(description = "ID of the instructor who created the course", example = "2")
    private Long creatorId;

    @Schema(description = "Course status", example = "PENDING_ADDITION")
    private CourseStatus status;

    @Schema(description = "When the course was created/submitted")
    private Instant createdAt;

    public static PendingCourseDto fromEntity(Course course) {
        return PendingCourseDto.builder()
                .id(course.getId())
                .courseName(course.getCourseName())
                .instructorName(course.getInstructor())
                .creatorEmail(course.getCreatedBy().getEmail())
                .creatorId(course.getCreatedBy().getId())
                .status(course.getStatus())
                .createdAt(course.getCreatedAt())
                .build();
    }
}