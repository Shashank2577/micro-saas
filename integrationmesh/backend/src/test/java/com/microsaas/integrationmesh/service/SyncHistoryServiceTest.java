package com.microsaas.integrationmesh.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.integrationmesh.model.SyncHistory;
import com.microsaas.integrationmesh.repository.SyncHistoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SyncHistoryServiceTest {

    @Mock
    private SyncHistoryRepository syncHistoryRepository;

    @InjectMocks
    private SyncHistoryService syncHistoryService;

    private UUID tenantId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testGetHistory() {
        UUID integrationId = UUID.randomUUID();
        when(syncHistoryRepository.findByIntegrationIdAndTenantIdOrderByStartedAtDesc(integrationId, tenantId)).thenReturn(List.of(new SyncHistory()));
        
        assertEquals(1, syncHistoryService.getHistory(integrationId).size());
    }
}
