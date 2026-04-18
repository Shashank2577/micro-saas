package com.microsaas.identitycore.service;

import com.microsaas.identitycore.model.AccessLog;
import com.microsaas.identitycore.repository.AccessLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AccessLogService {

    private final AccessLogRepository accessLogRepository;

    public AccessLogService(AccessLogRepository accessLogRepository) {
        this.accessLogRepository = accessLogRepository;
    }

    public List<AccessLog> getLogsByTenant(UUID tenantId) {
        return accessLogRepository.findByTenantId(tenantId);
    }

    @Transactional
    public AccessLog createLog(UUID tenantId, AccessLog log) {
        log.setId(UUID.randomUUID());
        log.setTenantId(tenantId);
        log.setCreatedAt(OffsetDateTime.now());
        return accessLogRepository.save(log);
    }
}
