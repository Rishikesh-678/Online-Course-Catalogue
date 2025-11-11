package com.edugate.edugateapi.repository;

import com.edugate.edugateapi.model.AdminLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminLogRepository extends JpaRepository<AdminLog, Long> {
    Page<AdminLog> findByAdmin_Id(Long adminId, Pageable pageable);
}