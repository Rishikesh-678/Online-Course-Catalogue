package com.edugate.edugateapi.controller;

import com.edugate.edugateapi.dto.UserProfileDto;
import com.edugate.edugateapi.dto.course.CourseResponse;
import com.edugate.edugateapi.model.User;
import com.edugate.edugateapi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class UserController {

    private final UserService userService;

    // --- Course Browsing & Subscription ---

    /**
     * Gets all *LIVE* courses for the main dashboard.
     */
    @GetMapping("/courses")
    public ResponseEntity<List<CourseResponse>> getAllLiveCourses() {
        return ResponseEntity.ok(userService.getAllLiveCourses());
    }

    /**
     * Gets all courses the logged-in user is subscribed to (for "My Courses" page).
     */
    @GetMapping("/courses/my-subscriptions")
    public ResponseEntity<List<CourseResponse>> getMySubscriptions(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(userService.getMySubscriptions(user));
    }

    /**
     * Subscribes the logged-in user to a course.
     */
    @PostMapping("/courses/subscribe/{courseId}")
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
    public ResponseEntity<UserProfileDto> updateMyProfile(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody UserProfileDto profileDto
    ) {
        UserProfileDto updatedProfile = userService.updateMyProfile(user, profileDto);
        return ResponseEntity.ok(updatedProfile);
    }
}