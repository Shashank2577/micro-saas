package com.microsaas.dataunification.service;

import com.microsaas.dataunification.model.AuditLog;
import com.microsaas.dataunification.repository.AuditLogRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;

@Service
public class AuditLogService {
    private final AuditLogRepository repository;

    public AuditLogService(AuditLogRepository repository) {
        this.repository = repository;
    }

    public List<AuditLog> findAll() {
        return repository.findByTenantId(TenantContext.require());
    }

    public AuditLog create(AuditLog log) {
        log.setId(UUID.randomUUID());
        log.setTenantId(TenantContext.require());
        log.setCreatedAt(LocalDateTime.now());
        return repository.save(log);
    }
}
