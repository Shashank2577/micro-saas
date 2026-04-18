package com.microsaas.integrationbridge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsaas.integrationbridge.model.FieldMapping;
import com.microsaas.integrationbridge.model.SyncJob;
import com.microsaas.integrationbridge.repository.FieldMappingRepository;
import com.microsaas.integrationbridge.repository.SyncJobRepository;
import com.microsaas.integrationbridge.repository.SyncedRecordRepository;
import com.microsaas.integrationbridge.service.AuditService;
import com.microsaas.integrationbridge.service.SyncService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SyncServiceTest {

    private SyncJobRepository syncJobRepository;
    private FieldMappingRepository fieldMappingRepository;
    private SyncedRecordRepository syncedRecordRepository;
    private AuditService auditService;
    private ObjectMapper objectMapper;
    private SyncService syncService;

    @BeforeEach
    void setUp() {
        syncJobRepository = Mockito.mock(SyncJobRepository.class);
        fieldMappingRepository = Mockito.mock(FieldMappingRepository.class);
        syncedRecordRepository = Mockito.mock(SyncedRecordRepository.class);
        auditService = Mockito.mock(AuditService.class);
        objectMapper = new ObjectMapper();
        syncService = new SyncService(syncJobRepository, fieldMappingRepository, syncedRecordRepository, auditService, objectMapper);
    }

    @Test
    void testExecuteSyncJob() {
        UUID jobId = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();
        UUID integrationId = UUID.randomUUID();
        
        SyncJob job = new SyncJob();
        job.setId(jobId);
        job.setTenantId(tenantId);
        job.setIntegrationId(integrationId);
        
        FieldMapping mapping = new FieldMapping();
        mapping.setSourceField("amount");
        mapping.setTargetField("transaction_amount");
        mapping.setTransformationRule("cents_to_dollars");
        
        when(syncJobRepository.findByIdAndTenantId(jobId, tenantId)).thenReturn(Optional.of(job));
        when(fieldMappingRepository.findBySyncJobIdAndTenantId(jobId, tenantId)).thenReturn(List.of(mapping));
        when(syncedRecordRepository.findByExternalIdAndIntegrationIdAndTenantId(anyString(), any(UUID.class), any(UUID.class))).thenReturn(Optional.empty());
        
        syncService.executeSyncJob(jobId, tenantId);
        
        verify(syncedRecordRepository, times(1)).save(any());
        verify(auditService, times(1)).log(eq(tenantId), eq(integrationId), eq("SYNC_JOB_RUN"), eq("SUCCESS"), eq(1), isNull());
    }
}
