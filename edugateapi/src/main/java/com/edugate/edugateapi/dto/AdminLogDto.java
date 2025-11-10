package com.edugate.edugateapi.dto;

import com.edugate.edugateapi.model.AdminLog;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class AdminLogDto {
    private Long id;
    private String adminEmail;
    private String action;
    private Long targetId;
    private String targetType;
    private String details;
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