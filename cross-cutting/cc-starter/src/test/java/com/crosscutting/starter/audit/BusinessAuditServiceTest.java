package com.crosscutting.starter.audit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BusinessAuditServiceTest {

    @Mock
    private BusinessAuditLogRepository repository;

    private BusinessAuditService service;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        service = new BusinessAuditService(repository, objectMapper);
        lenient().when(repository.save(any(BusinessAuditLog.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void recordCreatesAuditEntryWithCorrectFields() {
        UUID tenantId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID resourceId = UUID.randomUUID();

        service.record(tenantId, userId, "CREATE", "Order", resourceId,
                null, java.util.Map.of("id", resourceId.toString(), "total", 100));

        ArgumentCaptor<BusinessAuditLog> captor = ArgumentCaptor.forClass(BusinessAuditLog.class);
        verify(repository).save(captor.capture());

        BusinessAuditLog log = captor.getValue();
        assertEquals(tenantId, log.getTenantId());
        assertEquals(userId, log.getUserId());
        assertEquals("CREATE", log.getAction());
        assertEquals("Order", log.getResourceType());
        assertEquals(resourceId, log.getResourceId());
        assertNull(log.getBeforeState());
        assertNotNull(log.getAfterState());
        assertEquals(resourceId.toString(), log.getAfterState().get("id"));
    }

    @Test
    void recordHandlesNullBeforeAndAfterStates() {
        UUID tenantId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID resourceId = UUID.randomUUID();

        service.record(tenantId, userId, "DELETE", "Order", resourceId, null, null);

        ArgumentCaptor<BusinessAuditLog> captor = ArgumentCaptor.forClass(BusinessAuditLog.class);
        verify(repository).save(captor.capture());

        BusinessAuditLog log = captor.getValue();
        assertNull(log.getBeforeState());
        assertNull(log.getAfterState());
        assertEquals("DELETE", log.getAction());
    }

    @Test
    void recordDoesNotThrowWhenRepositoryFails() {
        when(repository.save(any())).thenThrow(new RuntimeException("DB error"));

        assertDoesNotThrow(() ->
                service.record(UUID.randomUUID(), UUID.randomUUID(), "CREATE", "Order",
                        UUID.randomUUID(), null, null));
    }
}
