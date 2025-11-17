package com.edugate.edugateapi.controller;

import com.edugate.edugateapi.dto.AdminLogDto;
import com.edugate.edugateapi.dto.PendingCourseDto;
import com.edugate.edugateapi.dto.UserDto;
import com.edugate.edugateapi.model.User;
import com.edugate.edugateapi.service.AdminService;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
// import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')") // This secures all endpoints in this controller
@Tag(name = "Admin Management", description = "Admin endpoints for user and course moderation")
public class AdminController {

    private final AdminService adminService;

    // --- User Management Endpoints (for "Manage Page") ---

    @GetMapping("/users")
    @Operation(summary = "Retrieve all users", description = "Returns a list of all registered users")
    @ApiResponse(responseCode = "200", description = "List of users", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":true,\"status\":200,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/admin/users\",\"message\":\"List of all users\",\"data\":[{\"id\":1,\"fullName\":\"John Doe\",\"email\":\"john@example.com\",\"phoneNumber\":\"+1-234-567-8900\",\"role\":\"ROLE_USER\"}]}")))
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":false,\"status\":401,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/admin/users\",\"message\":\"Unauthorized\",\"data\":null}")))
    @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":false,\"status\":403,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/admin/users\",\"message\":\"Forbidden - Admin role required\",\"data\":null}")))
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PutMapping("/users/promote/{userId}")
    @Operation(summary = "Promote user to instructor", description = "Promote a user to instructor role")
    @ApiResponse(
        responseCode = "200",
        description = "User promoted successfully",
        content = @Content(
            schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class),
            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":true,\"status\":200,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/admin/users/promote/1\",\"message\":\"User promoted successfully\",\"data\":{\"id\":1,\"fullName\":\"John Doe\",\"email\":\"john@example.com\",\"phoneNumber\":\"+1-234-567-8900\",\"role\":\"ROLE_INSTRUCTOR\"}}")
        )
    )
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":false,\"status\":401,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/admin/users/promote/1\",\"message\":\"Unauthorized\",\"data\":null}")))
    @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":false,\"status\":404,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/admin/users/promote/1\",\"message\":\"User not found\",\"data\":null}")))
    @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":false,\"status\":403,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/admin/users/promote/1\",\"message\":\"Forbidden - Admin role required\",\"data\":null}")))
    public ResponseEntity<UserDto> promoteUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal User admin
    ) {
        return ResponseEntity.ok(adminService.promoteUser(userId, admin));
    }

    @PutMapping("/users/demote/{userId}")
    @Operation(summary = "Demote instructor to user", description = "Demote an instructor back to a regular user role")
    @ApiResponse(
        responseCode = "200",
        description = "User demoted successfully",
        content = @Content(
            schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class),
            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":true,\"status\":200,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/admin/users/demote/1\",\"message\":\"User demoted successfully\",\"data\":{\"id\":1,\"fullName\":\"John Doe\",\"email\":\"john@example.com\",\"phoneNumber\":\"+1-234-567-8900\",\"role\":\"ROLE_USER\"}}")
        )
    )
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":false,\"status\":401,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/admin/users/demote/1\",\"message\":\"Unauthorized\",\"data\":null}")))
    @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":false,\"status\":404,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/admin/users/demote/1\",\"message\":\"User not found\",\"data\":null}")))
    @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":false,\"status\":403,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/admin/users/demote/1\",\"message\":\"Forbidden - Admin role required\",\"data\":null}")))
    public ResponseEntity<UserDto> demoteUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal User admin
    ) {
        return ResponseEntity.ok(adminService.demoteUser(userId, admin));
    }

    // --- Course Approval Endpoints (for Admin Landing Page) ---

    @GetMapping("/courses/pending")
    @Operation(summary = "Get pending courses", description = "Retrieve courses that are awaiting admin approval")
    @ApiResponse(responseCode = "200", description = "List of pending courses", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":true,\"status\":200,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/admin/courses/pending\",\"message\":\"Pending courses\",\"data\":[{\"id\":5,\"courseName\":\"Python Basics\",\"instructor\":\"Jane Smith\",\"category\":\"Programming\",\"videoLink\":\"https://youtube.com/watch?v=abc\",\"status\":\"PENDING_ADDITION\"}]}")))
    @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":false,\"status\":403,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/admin/courses/pending\",\"message\":\"Forbidden - Admin role required\",\"data\":null}")))
    public ResponseEntity<List<PendingCourseDto>> getPendingCourses() {
        return ResponseEntity.ok(adminService.getPendingCourses());
    }

    @PostMapping("/courses/approve/{courseId}")
    @Operation(summary = "Approve a pending course", description = "Approve and publish a pending course")
    @ApiResponse(responseCode = "200", description = "Course approved successfully", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":true,\"status\":200,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/admin/courses/approve/5\",\"message\":\"Course approved successfully\",\"data\":null}")))
    @ApiResponse(responseCode = "404", description = "Course not found", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":false,\"status\":404,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/admin/courses/approve/999\",\"message\":\"Course not found\",\"data\":null}")))
    @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":false,\"status\":403,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/admin/courses/approve/5\",\"message\":\"Forbidden - Admin role required\",\"data\":null}")))
    public ResponseEntity<com.edugate.edugateapi.dto.ApiResponse<Void>> approveCourse(
            @PathVariable Long courseId,
            @AuthenticationPrincipal User admin
    ) {
        adminService.approveCourse(courseId, admin);
        return ResponseEntity.ok(com.edugate.edugateapi.dto.ApiResponse.success(
            null,
            "Course approved successfully",
            "/api/admin/courses/approve/" + courseId,
            org.springframework.http.HttpStatus.OK
        ));
    }

    @PostMapping("/courses/reject/{courseId}")
    @Operation(summary = "Reject a pending course", description = "Reject a pending course with optional admin notes")
    @ApiResponse(responseCode = "200", description = "Course rejected successfully", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":true,\"status\":200,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/admin/courses/reject/5\",\"message\":\"Course rejected successfully\",\"data\":null}")))
    @ApiResponse(responseCode = "404", description = "Course not found", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":false,\"status\":404,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/admin/courses/reject/999\",\"message\":\"Course not found\",\"data\":null}")))
    @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":false,\"status\":403,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/admin/courses/reject/5\",\"message\":\"Forbidden - Admin role required\",\"data\":null}")))
    public ResponseEntity<com.edugate.edugateapi.dto.ApiResponse<Void>> rejectCourse(
            @PathVariable Long courseId,
            @AuthenticationPrincipal User admin
    ) {
        adminService.rejectCourse(courseId, admin);
        return ResponseEntity.ok(com.edugate.edugateapi.dto.ApiResponse.success(
            null,
            "Course rejected successfully",
            "/api/admin/courses/reject/" + courseId,
            org.springframework.http.HttpStatus.OK
        ));
    }

    // --- Admin Log Endpoint (for Admin Profile Page) ---

    // @GetMapping("/logs/me")
    // public ResponseEntity<Page<AdminLogDto>> getMyAdminLog(
    //         @AuthenticationPrincipal User admin,
    //         @PageableDefault(size = 10, sort = "createdAt,desc") Pageable pageable
    // ) {
    @GetMapping("/logs/me")
    @Operation(summary = "Get admin activity log", description = "Retrieve paged admin activity logs for the authenticated admin")
    @ApiResponse(responseCode = "200", description = "Paged admin logs", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":true,\"status\":200,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/admin/logs/me\",\"message\":\"Admin logs\",\"data\":{\"pageable\":{\"pageSize\":10,\"pageNumber\":0},\"content\":[{\"id\":1,\"action\":\"APPROVED_COURSE\",\"target\":\"Course 5\",\"createdAt\":\"2025-11-12T12:00:00Z\"}]}}")))
    @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":false,\"status\":403,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/admin/logs/me\",\"message\":\"Forbidden - Admin role required\",\"data\":null}")))
    public ResponseEntity<Page<AdminLogDto>> getMyAdminLog(
        @AuthenticationPrincipal User admin,
        Pageable pageable
    ) {
    // Force a safe sort (ignores Swagger’s ?sort=string)
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable safePageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                sort
      );

    return ResponseEntity.ok(adminService.getAdminLog(admin, safePageable));
}
}