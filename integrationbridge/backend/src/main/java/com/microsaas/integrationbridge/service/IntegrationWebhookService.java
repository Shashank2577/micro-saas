package com.microsaas.integrationbridge.service;

import com.microsaas.integrationbridge.model.SyncedRecord;
import com.microsaas.integrationbridge.repository.SyncedRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class IntegrationWebhookService {
    
    private final SyncedRecordRepository syncedRecordRepository;
    private final AuditService auditService;

    public IntegrationWebhookService(SyncedRecordRepository syncedRecordRepository, AuditService auditService) {
        this.syncedRecordRepository = syncedRecordRepository;
        this.auditService = auditService;
    }

    @Transactional
    public void processWebhook(String provider, UUID integrationId, UUID tenantId, String payload) {
        try {
            String externalId = "wh_" + System.currentTimeMillis();
            
            SyncedRecord record = new SyncedRecord();
            record.setTenantId(tenantId);
            record.setIntegrationId(integrationId);
            record.setExternalId(externalId);
            record.setData(payload);
            record.setSyncedAt(LocalDateTime.now());
            syncedRecordRepository.save(record);
            
            auditService.log(tenantId, integrationId, "WEBHOOK_RECEIVED", "SUCCESS", 1, null);
        } catch (Exception e) {
            auditService.log(tenantId, integrationId, "WEBHOOK_RECEIVED", "FAILED", 0, e.getMessage());
        }
    }
}
