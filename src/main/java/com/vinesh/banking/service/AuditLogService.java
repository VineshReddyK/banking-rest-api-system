package com.vinesh.banking.service;

import com.vinesh.banking.entity.AuditLog;
import com.vinesh.banking.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditLogService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    public void log(String action, String performedBy, String details) {
        auditLogRepository.save(new AuditLog(action, performedBy, details));
    }

    public List<AuditLog> getLogsForUser(String email) {
        return auditLogRepository.findByPerformedByOrderByTimestampDesc(email);
    }

    public List<AuditLog> getAllLogs() {
        return auditLogRepository.findAll();
    }
}
