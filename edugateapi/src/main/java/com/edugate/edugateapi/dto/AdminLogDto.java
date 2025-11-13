package com.edugate.edugateapi.dto;

import com.edugate.edugateapi.model.AdminLog;
import lombok.Builder;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Data
@Builder
@Schema(description = "Admin action log entry")
public class AdminLogDto {
    @Schema(description = "Unique identifier for the log entry", example = "1")
    private Long id;

    @Schema(description = "Email of the admin who performed the action", example = "admin@example.com")
    private String adminEmail;

    @Schema(description = "Type of action performed", example = "APPROVED_COURSE")
    private String action;

    @Schema(description = "ID of the target object (user or course)", example = "1")
    private Long targetId;

    @Schema(description = "Type of target object", example = "COURSE")
    private String targetType;

    @Schema(description = "Human-readable description of the action", example = "Approved and published new course: Spring Boot 101")
    private String details;

    @Schema(description = "Timestamp when the action was logged")
    private Instant createdAt;

    public static AdminLogDto fromEntity(AdminLog log) {
        return AdminLogDto.builder()
                .id(log.getId())
                .adminEmail(log.getAdmin().getEmail())
                .action(log.getAction())
                .targetId(log.getTargetId())
                .targetType(log.getTargetType())
                .details(log.getDetails())
                .createdAt(log.getCreatedAt())
                .build();
    }
}