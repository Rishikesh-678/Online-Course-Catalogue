package com.edugate.edugateapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "admin_log")
public class AdminLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Foreign Key to the User (Admin) who took the action
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private User admin;

    @Column(nullable = false)
    private String action; // e.g., "APPROVED_COURSE", "PROMOTED_USER"

    @Column(name = "target_id", nullable = false)
    private Long targetId; // The ID of the course or user affected

    @Column(name = "target_type", nullable = false)
    private String targetType; // "COURSE" or "USER"

    private String details; // "Approved course 'Spring Boot 101'"

    @Builder.Default
    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();
}