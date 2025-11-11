package com.edugate.edugateapi.dto;

import com.edugate.edugateapi.model.Course;
import com.edugate.edugateapi.model.CourseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PendingCourseDto {
    private Long id;
    private String courseName;
    private String instructorName; // The "Taken By" field
    private String creatorEmail; // The email of the instructor who submitted it
    private Long creatorId;
    private CourseStatus status;
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