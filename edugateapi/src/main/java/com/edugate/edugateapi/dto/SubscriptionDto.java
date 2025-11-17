package com.edugate.edugateapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

@Schema(description = "Subscription details for a user course subscription")
public class SubscriptionDto {
    @Schema(description = "User ID", example = "1")
    private Long userId;

    @Schema(description = "User's full name", example = "John Doe")
    private String userFullName;

    @Schema(description = "User's email", example = "john@example.com")
    private String userEmail;

    @Schema(description = "Course ID", example = "5")
    private Long courseId;

    @Schema(description = "Course name", example = "Java 101")
    private String courseName;

    @Schema(description = "Subscription timestamp", example = "2025-11-12T16:00:00Z")
    private Instant subscribedAt;

    public SubscriptionDto() {}

    public SubscriptionDto(Long userId, String userFullName, String userEmail, Long courseId, String courseName, Instant subscribedAt) {
        this.userId = userId;
        this.userFullName = userFullName;
        this.userEmail = userEmail;
        this.courseId = courseId;
        this.courseName = courseName;
        this.subscribedAt = subscribedAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Instant getSubscribedAt() {
        return subscribedAt;
    }

    public void setSubscribedAt(Instant subscribedAt) {
        this.subscribedAt = subscribedAt;
    }
}
