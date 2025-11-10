package com.edugate.edugateapi.service;

import com.edugate.edugateapi.model.AdminLog;
import com.edugate.edugateapi.model.User;
import com.edugate.edugateapi.repository.AdminLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminLogService {

    private final AdminLogRepository adminLogRepository;

    /**
     * Helper method to create a new log entry.
     * @param admin The admin user performing the action.
     * @param action The action type (e.g., "PROMOTED_USER").
     * @param targetId The ID of the object being changed (a user ID or course ID).
     * @param targetType The type of object ("USER" or "COURSE").
     * @param details A human-readable description of the action.
     */
    public void logAction(User admin, String action, Long targetId, String targetType, String details) {
        if (admin == null || !admin.getRole().equals(com.edugate.edugateapi.model.Role.ROLE_ADMIN)) {
            // Or throw an exception
            return; 
        }

        AdminLog logEntry = AdminLog.builder()
                .admin(admin)
                .action(action)
                .targetId(targetId)
                .targetType(targetType)
                .details(details)
                .build();
        
        adminLogRepository.save(logEntry);
    }
}