package com.vinesh.banking.service;

import com.vinesh.banking.entity.AuditLog;
import com.vinesh.banking.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// thin wrapper around the audit_log table — every security-relevant
// action (login, register, deposit, withdrawal, transfer) gets recorded
// here so there's an immutable trail for compliance reviews.
@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void log(String action, String performedBy, String details) {
        AuditLog entry = new AuditLog(action, performedBy, details);
        auditLogRepository.save(entry);
    }

    public List<AuditLog> getLogsForUser(String email) {
        return auditLogRepository.findByPerformedByOrderByTimestampDesc(email);
    }

    public List<AuditLog> getAllLogs() {
        // no filtering — returns everything, caller is responsible for pagination if needed
        return auditLogRepository.findAll();
    }
}
