package com.edugate.edugateapi.controller;

import com.edugate.edugateapi.dto.UserProfileDto;
import com.edugate.edugateapi.dto.course.CourseResponse;
import com.edugate.edugateapi.model.User;
import com.edugate.edugateapi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
// Secures all endpoints for any authenticated user (USER, INSTRUCTOR, or ADMIN)
@PreAuthorize("isAuthenticated()") 
@Tag(name = "User Management", description = "Endpoints for browsing courses, subscriptions and profile management")
public class UserController {

    private final UserService userService;

    // --- Course Browsing & Subscription ---

    /**
     * Gets all *LIVE* courses for the main dashboard.
     */
    @GetMapping("/courses")
    @Operation(summary = "Get live courses", description = "Retrieve all courses that are currently live/published")
    @ApiResponse(responseCode = "200", description = "List of live courses", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":true,\"status\":200,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/user/courses\",\"message\":\"Live courses retrieved\",\"data\":[{\"id\":1,\"title\":\"Intro to Java\",\"summary\":\"Basics of Java programming\"}]}")))
    @ApiResponse(responseCode = "401", description = "Unauthorized - Valid JWT required", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":false,\"status\":401,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/user/courses\",\"message\":\"Unauthorized - Valid JWT required\",\"data\":null}")))
    public ResponseEntity<List<CourseResponse>> getAllLiveCourses() {
        return ResponseEntity.ok(userService.getAllLiveCourses());
    }

    /**
     * Gets all courses the logged-in user is subscribed to (for "My Courses" page).
     */
    @GetMapping("/courses/my-subscriptions")
    @Operation(summary = "Get my subscriptions", description = "Retrieve the list of courses the current user is subscribed to")
    @ApiResponse(responseCode = "200", description = "List of subscriptions", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":true,\"status\":200,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/user/courses/my-subscriptions\",\"message\":\"Subscriptions retrieved\",\"data\":[{\"id\":2,\"title\":\"Spring Boot Basics\",\"summary\":\"Build REST APIs with Spring Boot\"}]}")))
    @ApiResponse(responseCode = "401", description = "Unauthorized - Valid JWT required", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":false,\"status\":401,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/user/courses/my-subscriptions\",\"message\":\"Unauthorized - Valid JWT required\",\"data\":null}")))
    public ResponseEntity<List<CourseResponse>> getMySubscriptions(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(userService.getMySubscriptions(user));
    }

    /**
     * Subscribes the logged-in user to a course.
     */
    @PostMapping("/courses/subscribe/{courseId}")
    @Operation(summary = "Subscribe to a course", description = "Subscribe the authenticated user to a course by ID")
    @ApiResponse(responseCode = "200", description = "Successfully subscribed to course", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":true,\"status\":200,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/user/courses/subscribe/1\",\"message\":\"Successfully subscribed to course\",\"data\":null}")))
    @ApiResponse(responseCode = "400", description = "Bad request - course not approved", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":false,\"status\":400,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/user/courses/subscribe/1\",\"message\":\"Cannot subscribe to a non-approved course.\",\"data\":null}")))
    @ApiResponse(responseCode = "401", description = "Unauthorized - Valid JWT required", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":false,\"status\":401,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/user/courses/subscribe/1\",\"message\":\"Unauthorized - Valid JWT required\",\"data\":null}")))
    @ApiResponse(responseCode = "404", description = "Course not found", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":false,\"status\":404,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/user/courses/subscribe/1\",\"message\":\"Course not found\",\"data\":null}")))
    @ApiResponse(responseCode = "409", description = "Conflict - Already subscribed to this course", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":false,\"status\":409,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/user/courses/subscribe/1\",\"message\":\"Conflict - Already subscribed to this course\",\"data\":null}")))
    public ResponseEntity<Void> subscribeToCourse(
            @PathVariable Long courseId,
            @AuthenticationPrincipal User user
    ) {
        userService.subscribeToCourse(courseId, user);
        return ResponseEntity.ok().build();
    }

    /**
     * Unsubscribes the logged-in user from a course.
     */
    @DeleteMapping("/courses/unsubscribe/{courseId}")
    @Operation(summary = "Unsubscribe from a course", description = "Remove the authenticated user's subscription to the course")
    @ApiResponse(responseCode = "204", description = "Successfully unsubscribed from course", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":true,\"status\":204,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/user/courses/unsubscribe/1\",\"message\":\"Successfully unsubscribed from course\",\"data\":null}")))
    @ApiResponse(responseCode = "401", description = "Unauthorized - Valid JWT required", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":false,\"status\":401,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/user/courses/unsubscribe/1\",\"message\":\"Unauthorized - Valid JWT required\",\"data\":null}")))
    @ApiResponse(responseCode = "404", description = "Subscription not found", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":false,\"status\":404,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/user/courses/unsubscribe/1\",\"message\":\"Subscription not found\",\"data\":null}")))
    public ResponseEntity<Void> unsubscribeFromCourse(
            @PathVariable Long courseId,
            @AuthenticationPrincipal User user
    ) {
        userService.unsubscribeFromCourse(courseId, user);
        return ResponseEntity.noContent().build();
    }

    // --- Profile Management ---

    /**
     * Gets the logged-in user's profile details.
     */
    @GetMapping("/profile/me")
    @Operation(summary = "Get my profile", description = "Retrieve profile information of the authenticated user")
    @ApiResponse(
        responseCode = "200",
        description = "Successfully retrieved user profile",
        content = @Content(
            schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class),
            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":true,\"status\":200,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/user/profile/me\",\"message\":\"Successfully retrieved user profile\",\"data\":{\"id\":10,\"fullName\":\"John Doe\",\"email\":\"john@example.com\",\"phoneNumber\":\"+1-234-567-8900\",\"role\":\"ROLE_USER\"}}")
        )
    )
    @ApiResponse(responseCode = "401", description = "Unauthorized - Valid JWT required", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":false,\"status\":401,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/user/profile/me\",\"message\":\"Unauthorized - Valid JWT required\",\"data\":null}")))
    public ResponseEntity<UserProfileDto> getMyProfile(
            @AuthenticationPrincipal User user
    ) {
        UserProfileDto profile = new UserProfileDto();
        profile.setFullName(user.getFullName());
        profile.setPhoneNumber(user.getPhoneNumber());
        return ResponseEntity.ok(profile);
    }

    /**
     * Updates the logged-in user's profile details.
     */
    @PutMapping("/profile/me")
    @Operation(summary = "Update my profile", description = "Updates the profile information (full name and phone number) of the authenticated user.")
    @ApiResponse(
        responseCode = "200",
        description = "Profile updated successfully",
        content = @Content(
            schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class),
            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":true,\"status\":200,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/user/profile/me\",\"message\":\"Profile updated successfully\",\"data\":{\"id\":10,\"fullName\":\"Jane Doe\",\"email\":\"jane@example.com\",\"phoneNumber\":\"+1-234-567-8910\",\"role\":\"ROLE_USER\"}}")
        )
    )
    @ApiResponse(responseCode = "400", description = "Bad request - validation failed", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":false,\"status\":400,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/user/profile/me\",\"message\":\"Bad request - validation failed\",\"data\":null}")))
    @ApiResponse(responseCode = "401", description = "Unauthorized - Valid JWT required", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":false,\"status\":401,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/user/profile/me\",\"message\":\"Unauthorized - Valid JWT required\",\"data\":null}")))
    public ResponseEntity<UserProfileDto> updateMyProfile(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody UserProfileDto profileDto
    ) {
        UserProfileDto updatedProfile = userService.updateMyProfile(user, profileDto);
        return ResponseEntity.ok(updatedProfile);
    }
}