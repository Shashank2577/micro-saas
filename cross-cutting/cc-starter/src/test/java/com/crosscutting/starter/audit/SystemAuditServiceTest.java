package com.crosscutting.starter.audit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SystemAuditServiceTest {

    @Mock
    private SystemAuditLogRepository repository;

    private SystemAuditService service;

    @BeforeEach
    void setUp() {
        service = new SystemAuditService(repository);
    }

    @Test
    void findByEventTypeReturnsFilteredLogs() {
        Pageable pageable = PageRequest.of(0, 10);
        SystemAuditLog log = new SystemAuditLog();
        log.setEventType("API");
        Page<SystemAuditLog> expected = new PageImpl<>(List.of(log));

        when(repository.findByEventType("API", pageable)).thenReturn(expected);

        Page<SystemAuditLog> result = service.findByEventType("API", pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("API", result.getContent().get(0).getEventType());
        verify(repository).findByEventType("API", pageable);
    }

    @Test
    void findByTenantAndEventTypeReturnsFilteredLogs() {
        UUID tenantId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);
        SystemAuditLog log = new SystemAuditLog();
        log.setTenantId(tenantId);
        log.setEventType("AUTH");
        Page<SystemAuditLog> expected = new PageImpl<>(List.of(log));

        when(repository.findByTenantIdAndEventType(tenantId, "AUTH", pageable)).thenReturn(expected);

        Page<SystemAuditLog> result = service.findByTenantAndEventType(tenantId, "AUTH", pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(tenantId, result.getContent().get(0).getTenantId());
        assertEquals("AUTH", result.getContent().get(0).getEventType());
    }

    @Test
    void findByTenantDelegatesToRepository() {
        UUID tenantId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 20);
        Page<SystemAuditLog> expected = new PageImpl<>(List.of());

        when(repository.findByTenantIdIncludingNull(tenantId, pageable)).thenReturn(expected);

        Page<SystemAuditLog> result = service.findByTenant(tenantId, pageable);

        assertEquals(0, result.getTotalElements());
        verify(repository).findByTenantIdIncludingNull(tenantId, pageable);
    }

    @Test
    void findByUserDelegatesToRepository() {
        UUID userId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);
        SystemAuditLog log = new SystemAuditLog();
        log.setUserId(userId);
        Page<SystemAuditLog> expected = new PageImpl<>(List.of(log));

        when(repository.findByUserId(userId, pageable)).thenReturn(expected);

        Page<SystemAuditLog> result = service.findByUser(userId, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(userId, result.getContent().get(0).getUserId());
    }

    @Test
    void findByResourceDelegatesToRepository() {
        Pageable pageable = PageRequest.of(0, 10);
        String resourceType = "Order";
        String resourceId = "123";
        Page<SystemAuditLog> expected = new PageImpl<>(List.of());

        when(repository.findByResourceTypeAndResourceId(resourceType, resourceId, pageable))
                .thenReturn(expected);

        Page<SystemAuditLog> result = service.findByResource(resourceType, resourceId, pageable);

        assertEquals(0, result.getTotalElements());
        verify(repository).findByResourceTypeAndResourceId(resourceType, resourceId, pageable);
    }
}
