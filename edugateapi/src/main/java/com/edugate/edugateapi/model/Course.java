package com.edugate.edugateapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    private String instructor;
    private String category;

    @Column(name = "video_link", length = 1024)
    private String videoLink;

    // This will store the FILENAME of the uploaded icon, not the file itself
    @Column(length = 1024)
    private String thumbnail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseStatus status;

    // Foreign Key to the User (Instructor) who created it
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id", nullable = false)
    private User createdBy;

    @Builder.Default
    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserSubscription> subscriptions;
}