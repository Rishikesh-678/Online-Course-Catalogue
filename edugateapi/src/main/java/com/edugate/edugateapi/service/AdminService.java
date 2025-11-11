package com.edugate.edugateapi.service;

import com.edugate.edugateapi.dto.AdminLogDto;
import com.edugate.edugateapi.dto.PendingCourseDto;
import com.edugate.edugateapi.dto.UserDto;
import com.edugate.edugateapi.exception.ResourceNotFoundException; // <-- CHANGED
import com.edugate.edugateapi.model.Course;
import com.edugate.edugateapi.model.CourseStatus;
import com.edugate.edugateapi.model.Role;
import com.edugate.edugateapi.model.User;
import com.edugate.edugateapi.repository.AdminLogRepository;
import com.edugate.edugateapi.repository.CourseRepository;
import com.edugate.edugateapi.repository.UserRepository;
import com.edugate.edugateapi.repository.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
// import org.springframework.security.core.userdetails.UsernameNotFoundException; // <-- REMOVED
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final AdminLogRepository adminLogRepository;
    private final AdminLogService adminLogService;
    private final UserSubscriptionRepository subscriptionRepository;

    // --- User Management ---

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserDto promoteUser(Long userId, User admin) {
        User userToPromote = findUserById(userId);
        if (userToPromote.getRole().equals(Role.ROLE_USER)) {
            userToPromote.setRole(Role.ROLE_INSTRUCTOR);
            userRepository.save(userToPromote);

            // Log the action
            adminLogService.logAction(admin, "PROMOTED_USER", userId, "USER",
                    "Promoted user " + userToPromote.getEmail() + " to INSTRUCTOR");
        }
        return UserDto.fromEntity(userToPromote);
    }

    @Transactional
    public UserDto demoteUser(Long userId, User admin) {
        User userToDemote = findUserById(userId);
        if (userToDemote.getRole().equals(Role.ROLE_INSTRUCTOR)) {
            userToDemote.setRole(Role.ROLE_USER);
            userRepository.save(userToDemote);

            // Log the action
            adminLogService.logAction(admin, "DEMOTED_USER", userId, "USER",
                    "Demoted instructor " + userToDemote.getEmail() + " to USER");
        }
        return UserDto.fromEntity(userToDemote);
    }

    // --- Course Approval ---

    public List<PendingCourseDto> getPendingCourses() {
        List<CourseStatus> pendingStatuses = List.of(
                CourseStatus.PENDING_ADDITION, 
                CourseStatus.PENDING_REMOVAL
        );
        return courseRepository.findByStatusIn(pendingStatuses).stream()
                .map(PendingCourseDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public void approveCourse(Long courseId, User admin) {
        Course course = findCourseById(courseId);
        String details;

        if (course.getStatus().equals(CourseStatus.PENDING_ADDITION)) {
            course.setStatus(CourseStatus.APPROVED);
            details = "Approved and published new course: " + course.getCourseName();
            adminLogService.logAction(admin, "APPROVED_COURSE", courseId, "COURSE", details);
            courseRepository.save(course);

        } else if (course.getStatus().equals(CourseStatus.PENDING_REMOVAL)) {
            details = "Approved removal of course: " + course.getCourseName();
            // We log *before* deleting so we have a record
            adminLogService.logAction(admin, "APPROVED_REMOVAL", courseId, "COURSE", details);

            // --- THIS IS THE FIX ---
            // 1. Manually delete all subscriptions referencing this course
            subscriptionRepository.deleteByCourseId(courseId);
            
            // 2. Now we can safely delete the course
            courseRepository.delete(course);
            // ---------------------
        }
    }

    @Transactional
    public void rejectCourse(Long courseId, User admin) {
        Course course = findCourseById(courseId);
        String details;

        if (course.getStatus().equals(CourseStatus.PENDING_ADDITION)) {
            details = "Rejected new course submission: " + course.getCourseName();
            // Log before deleting
            adminLogService.logAction(admin, "REJECTED_ADDITION", courseId, "COURSE", details);
            courseRepository.delete(course);
        } else if (course.getStatus().equals(CourseStatus.PENDING_REMOVAL)) {
            course.setStatus(CourseStatus.APPROVED);
            details = "Rejected removal request for course: " + course.getCourseName();
            adminLogService.logAction(admin, "REJECTED_REMOVAL", courseId, "COURSE", details);
            courseRepository.save(course);
        }
    }

    // --- Admin Log ---

    public Page<AdminLogDto> getAdminLog(User admin, Pageable pageable) {
        return adminLogRepository.findByAdmin_Id(admin.getId(), pageable)
                .map(AdminLogDto::fromEntity);
  }


    // --- Helper Methods ---

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                // v-- CHANGED --v
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    private Course findCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                 // v-- CHANGED --v
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));
    }
}