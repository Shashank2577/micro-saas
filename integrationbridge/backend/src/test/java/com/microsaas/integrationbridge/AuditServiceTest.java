package com.microsaas.integrationbridge;

import com.microsaas.integrationbridge.model.AuditLog;
import com.microsaas.integrationbridge.repository.AuditLogRepository;
import com.microsaas.integrationbridge.service.AuditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class AuditServiceTest {
    private AuditLogRepository repository;
    private AuditService service;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(AuditLogRepository.class);
        service = new AuditService(repository);
    }

    @Test
    void testLog() {
        UUID tenantId = UUID.randomUUID();
        UUID integrationId = UUID.randomUUID();
        
        service.log(tenantId, integrationId, "ACTION", "SUCCESS", 10, null);
        
        verify(repository, times(1)).save(any(AuditLog.class));
    }
}
