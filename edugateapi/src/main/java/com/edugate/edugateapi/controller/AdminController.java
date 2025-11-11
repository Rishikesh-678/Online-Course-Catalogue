package com.edugate.edugateapi.controller;

import com.edugate.edugateapi.dto.AdminLogDto;
import com.edugate.edugateapi.dto.PendingCourseDto;
import com.edugate.edugateapi.dto.UserDto;
import com.edugate.edugateapi.model.User;
import com.edugate.edugateapi.service.AdminService;
import lombok.RequiredArgsConstructor;
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
public class AdminController {

    private final AdminService adminService;

    // --- User Management Endpoints (for "Manage Page") ---

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PutMapping("/users/promote/{userId}")
    public ResponseEntity<UserDto> promoteUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal User admin
    ) {
        return ResponseEntity.ok(adminService.promoteUser(userId, admin));
    }

    @PutMapping("/users/demote/{userId}")
    public ResponseEntity<UserDto> demoteUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal User admin
    ) {
        return ResponseEntity.ok(adminService.demoteUser(userId, admin));
    }

    // --- Course Approval Endpoints (for Admin Landing Page) ---

    @GetMapping("/courses/pending")
    public ResponseEntity<List<PendingCourseDto>> getPendingCourses() {
        return ResponseEntity.ok(adminService.getPendingCourses());
    }

    @PostMapping("/courses/approve/{courseId}")
    public ResponseEntity<Void> approveCourse(
            @PathVariable Long courseId,
            @AuthenticationPrincipal User admin
    ) {
        adminService.approveCourse(courseId, admin);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/courses/reject/{courseId}")
    public ResponseEntity<Void> rejectCourse(
            @PathVariable Long courseId,
            @AuthenticationPrincipal User admin
    ) {
        adminService.rejectCourse(courseId, admin);
        return ResponseEntity.ok().build();
    }

    // --- Admin Log Endpoint (for Admin Profile Page) ---

    // @GetMapping("/logs/me")
    // public ResponseEntity<Page<AdminLogDto>> getMyAdminLog(
    //         @AuthenticationPrincipal User admin,
    //         @PageableDefault(size = 10, sort = "createdAt,desc") Pageable pageable
    // ) {
    @GetMapping("/logs/me")
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