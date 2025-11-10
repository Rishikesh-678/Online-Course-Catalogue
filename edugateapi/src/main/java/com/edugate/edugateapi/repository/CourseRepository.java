package com.edugate.edugateapi.repository;

import com.edugate.edugateapi.model.Course;
import com.edugate.edugateapi.model.CourseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    // Find courses for the admin's "Pending Requests" dashboard
    List<Course> findByStatusIn(List<CourseStatus> statuses);

    // Find all *live* courses for regular users
    List<Course> findByStatus(CourseStatus status);
    List<Course> findByCreatedById(Long userId);
    @Query("SELECT c FROM Course c JOIN c.subscriptions s WHERE s.user.id = :userId")
    List<Course> findCoursesBySubscriberId(@Param("userId") Long userId);
}