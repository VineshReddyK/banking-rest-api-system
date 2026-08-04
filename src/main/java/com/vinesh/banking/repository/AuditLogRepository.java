package com.vinesh.banking.repository;

import com.vinesh.banking.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// sorted newest-first so the most recent action is always at the top
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByPerformedByOrderByTimestampDesc(String performedBy);
}
