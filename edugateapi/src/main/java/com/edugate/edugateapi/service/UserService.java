// package com.edugate.edugateapi.service;

// import com.edugate.edugateapi.dto.SubscriptionDto;
// import com.edugate.edugateapi.dto.UserProfileDto;
// import com.edugate.edugateapi.dto.course.CourseResponse;
// import com.edugate.edugateapi.exception.BadRequestException;
// import com.edugate.edugateapi.exception.ConflictException;
// import com.edugate.edugateapi.exception.ResourceNotFoundException; // <-- CHANGED
// import com.edugate.edugateapi.model.*;
// import com.edugate.edugateapi.repository.CourseRepository;
// import com.edugate.edugateapi.repository.UserRepository;
// import com.edugate.edugateapi.repository.UserSubscriptionRepository;
// import lombok.RequiredArgsConstructor;
// // import org.springframework.security.core.userdetails.UsernameNotFoundException; // <-- REMOVED
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;
// import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

// import java.util.List;
// import java.util.stream.Collectors;

// @Service
// @RequiredArgsConstructor
// public class UserService {

//     private final CourseRepository courseRepository;
//     private final UserRepository userRepository;
//     private final UserSubscriptionRepository subscriptionRepository;

//     // Helper to get the base URL (e.g., http://localhost:8080)
//     private String getBaseUrl() {
//         return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
//     }

//     /**
//      * Fetches all *APPROVED* courses for users to browse.
//      */
//     public List<CourseResponse> getAllLiveCourses() {
//         return courseRepository.findByStatus(CourseStatus.APPROVED).stream()
//                 .map(course -> CourseResponse.fromEntity(course, getBaseUrl()))
//                 .collect(Collectors.toList());
//     }

//     /**
//      * Subscribes a user to a specific course and returns subscription details.
//      */
//     @Transactional
//     public SubscriptionDto subscribeToCourse(Long courseId, User user) {
//         Course course = courseRepository.findById(courseId)
//                  // v-- CHANGED --v
//                 .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

//         if (!course.getStatus().equals(CourseStatus.APPROVED)) {
//             throw new BadRequestException("Cannot subscribe to a non-approved course.");
//         }

//         // Create the composite key
//         UserSubscriptionId id = new UserSubscriptionId(user.getId(), courseId);

//         // Check if already subscribed
//         if (subscriptionRepository.existsById(id)) {
//             throw new ConflictException("User is already subscribed to this course.");
//         }

//         // Create the new subscription link
//         UserSubscription subscription = UserSubscription.builder()
//                 .id(id)
//                 .user(user)
//                 .course(course)
//                 .build();
        
//         UserSubscription savedSubscription = subscriptionRepository.save(subscription);
        
//         // Return subscription details
//         return new SubscriptionDto(
//                 user.getId(),
//                 user.getFullName(),
//                 user.getEmail(),
//                 course.getId(),
//                 course.getCourseName(),
//                 savedSubscription.getSubscribedAt()
//         );
//     }

//     /**
//      * Unsubscribes a user from a specific course.
//      */
//     @Transactional
//     public void unsubscribeFromCourse(Long courseId, User user) {
//         // Create the composite key
//         UserSubscriptionId id = new UserSubscriptionId(user.getId(), courseId);

//         // Check if the subscription exists
//         if (!subscriptionRepository.existsById(id)) {
//              // v-- CHANGED --v
//             throw new ResourceNotFoundException("Subscription not found for user " + user.getId() + " and course " + courseId);
//         }

//         subscriptionRepository.deleteById(id);
//     }

//     /**
//      * Fetches all courses a user is currently subscribed to.
//      */
//     public List<CourseResponse> getMySubscriptions(User user) {
//         // Use the new custom query from CourseRepository
//         return courseRepository.findCoursesBySubscriberId(user.getId()).stream()
//                 .map(course -> CourseResponse.fromEntity(course, getBaseUrl()))
//                 .collect(Collectors.toList());
//     }

//     /**
//      * Updates a user's own profile information.
//      */
//     @Transactional
//     public UserProfileDto updateMyProfile(User user, UserProfileDto profileDto) {
//         // We find the user again from the repository to ensure we have the latest data
//         User userToUpdate = userRepository.findById(user.getId())
//                  // v-- CHANGED --v
//                 .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + user.getId()));

//         userToUpdate.setFullName(profileDto.getFullName());
//         userToUpdate.setPhoneNumber(profileDto.getPhoneNumber());

//         User savedUser = userRepository.save(userToUpdate);

//         // Return the updated DTO
//         UserProfileDto updatedDto = new UserProfileDto();
//         updatedDto.setFullName(savedUser.getFullName());
//         updatedDto.setPhoneNumber(savedUser.getPhoneNumber());
//         return updatedDto;
//     }
// }
package com.edugate.edugateapi.service;

import com.edugate.edugateapi.dto.UserProfileDto;
import com.edugate.edugateapi.dto.auth.ChangePasswordRequest;
import com.edugate.edugateapi.dto.course.CourseResponse;
import com.edugate.edugateapi.exception.BadRequestException;
import com.edugate.edugateapi.exception.ConflictException;
import com.edugate.edugateapi.exception.ResourceNotFoundException; // <-- CHANGED
import com.edugate.edugateapi.model.*;
import com.edugate.edugateapi.repository.CourseRepository;
import com.edugate.edugateapi.repository.UserRepository;
import com.edugate.edugateapi.repository.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
// import org.springframework.security.core.userdetails.UsernameNotFoundException; // <-- REMOVED
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final UserSubscriptionRepository subscriptionRepository;
    private final PasswordEncoder passwordEncoder;

    // Helper to get the base URL (e.g., http://localhost:8080)
    private String getBaseUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    }

    /**
     * Fetches all *APPROVED* courses for users to browse.
     */
    public List<CourseResponse> getAllLiveCourses() {
        return courseRepository.findByStatus(CourseStatus.APPROVED).stream()
                .map(course -> CourseResponse.fromEntity(course, getBaseUrl()))
                .collect(Collectors.toList());
    }

    /**
     * Fetches a single approved course by its ID.
     */
    public CourseResponse getLiveCourseById(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        if (!course.getStatus().equals(CourseStatus.APPROVED)) {
            // Or, you could show PENDING courses to the instructor who owns them
            throw new BadRequestException("This course is not currently available.");
        }
        
        return CourseResponse.fromEntity(course, getBaseUrl());
    }

    /**
     * Subscribes a user to a specific course.
     */
    @Transactional
    public void subscribeToCourse(Long courseId, User user) {
        Course course = courseRepository.findById(courseId)
                 // v-- CHANGED --v
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        if (!course.getStatus().equals(CourseStatus.APPROVED)) {
            throw new BadRequestException("Cannot subscribe to a non-approved course.");
        }

        // Create the composite key
        UserSubscriptionId id = new UserSubscriptionId(user.getId(), courseId);

        // Check if already subscribed
        if (subscriptionRepository.existsById(id)) {
            throw new ConflictException("User is already subscribed to this course.");
        }

        // Create the new subscription link
        UserSubscription subscription = UserSubscription.builder()
                .id(id)
                .user(user)
                .course(course)
                .build();
        
        subscriptionRepository.save(subscription);
    }

    /**
     * Unsubscribes a user from a specific course.
     */
    @Transactional
    public void unsubscribeFromCourse(Long courseId, User user) {
        // Create the composite key
        UserSubscriptionId id = new UserSubscriptionId(user.getId(), courseId);

        // Check if the subscription exists
        if (!subscriptionRepository.existsById(id)) {
             // v-- CHANGED --v
            throw new ResourceNotFoundException("Subscription not found for user " + user.getId() + " and course " + courseId);
        }

        subscriptionRepository.deleteById(id);
    }

    /**
     * Fetches all courses a user is currently subscribed to.
     */
    public List<CourseResponse> getMySubscriptions(User user) {
        // Use the new custom query from CourseRepository
        return courseRepository.findCoursesBySubscriberId(user.getId()).stream()
                .map(course -> CourseResponse.fromEntity(course, getBaseUrl()))
                .collect(Collectors.toList());
    }

    /**
     * Updates a user's own profile information.
     */
    @Transactional
    public UserProfileDto updateMyProfile(User user, UserProfileDto profileDto) {
        // We find the user again from the repository to ensure we have the latest data
        User userToUpdate = userRepository.findById(user.getId())
                 // v-- CHANGED --v
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + user.getId()));

        userToUpdate.setFullName(profileDto.getFullName());
        userToUpdate.setPhoneNumber(profileDto.getPhoneNumber());

        User savedUser = userRepository.save(userToUpdate);

        // Return the updated DTO
        UserProfileDto updatedDto = new UserProfileDto();
        updatedDto.setFullName(savedUser.getFullName());
        updatedDto.setPhoneNumber(savedUser.getPhoneNumber());
        return updatedDto;
    }

    /**
     * Changes a user's password.
     */
    @Transactional
    public void changePassword(User user, ChangePasswordRequest request) {
        User userToUpdate = userRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + user.getId()));

        // Verify the current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), userToUpdate.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }

        // Check that new password is different from current password
        if (passwordEncoder.matches(request.getNewPassword(), userToUpdate.getPassword())) {
            throw new BadRequestException("New password must be different from the current password");
        }

        // Encode and set the new password
        userToUpdate.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(userToUpdate);
    }
}