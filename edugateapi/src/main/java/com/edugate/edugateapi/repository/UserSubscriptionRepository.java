package com.edugate.edugateapi.repository;

import com.edugate.edugateapi.model.UserSubscription;
import com.edugate.edugateapi.model.UserSubscriptionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying; // <-- ADD THIS IMPORT
import org.springframework.data.jpa.repository.Query;  // <-- ADD THIS IMPORT
import org.springframework.data.repository.query.Param;

public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, UserSubscriptionId> {
    // We can add custom queries here later, e.g., "find by user id"
    @Modifying // Required for DELETE or UPDATE operations
    @Query("DELETE FROM UserSubscription us WHERE us.course.id = :courseId")
    void deleteByCourseId(@Param("courseId") Long courseId);
}