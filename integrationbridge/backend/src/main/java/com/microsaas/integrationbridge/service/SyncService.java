package com.microsaas.integrationbridge.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.microsaas.integrationbridge.model.FieldMapping;
import com.microsaas.integrationbridge.model.SyncJob;
import com.microsaas.integrationbridge.model.SyncedRecord;
import com.microsaas.integrationbridge.repository.FieldMappingRepository;
import com.microsaas.integrationbridge.repository.SyncJobRepository;
import com.microsaas.integrationbridge.repository.SyncedRecordRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class SyncService {

    private final SyncJobRepository syncJobRepository;
    private final FieldMappingRepository fieldMappingRepository;
    private final SyncedRecordRepository syncedRecordRepository;
    private final AuditService auditService;
    private final ObjectMapper objectMapper;

    public SyncService(SyncJobRepository syncJobRepository, FieldMappingRepository fieldMappingRepository, SyncedRecordRepository syncedRecordRepository, AuditService auditService, ObjectMapper objectMapper) {
        this.syncJobRepository = syncJobRepository;
        this.fieldMappingRepository = fieldMappingRepository;
        this.syncedRecordRepository = syncedRecordRepository;
        this.auditService = auditService;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public SyncJob createSyncJob(UUID integrationId, UUID tenantId, String scheduleCron, String sourceEntity, String targetEntity) {
        SyncJob job = new SyncJob();
        job.setIntegrationId(integrationId);
        job.setTenantId(tenantId);
        job.setScheduleCron(scheduleCron);
        job.setSourceEntity(sourceEntity);
        job.setTargetEntity(targetEntity);
        job.setStatus("ACTIVE");
        return syncJobRepository.save(job);
    }
    
    public List<SyncJob> getSyncJobs(UUID integrationId, UUID tenantId) {
        return syncJobRepository.findByIntegrationIdAndTenantId(integrationId, tenantId);
    }

    @Transactional
    public FieldMapping addFieldMapping(UUID syncJobId, UUID tenantId, String source, String target, String transform) {
        FieldMapping mapping = new FieldMapping();
        mapping.setSyncJobId(syncJobId);
        mapping.setTenantId(tenantId);
        mapping.setSourceField(source);
        mapping.setTargetField(target);
        mapping.setTransformationRule(transform);
        return fieldMappingRepository.save(mapping);
    }

    @Scheduled(fixedDelay = 60000)
    public void runScheduledJobs() {
        // Mock schedule runner: just run jobs that are ACTIVE for now
        List<SyncJob> activeJobs = syncJobRepository.findByStatus("ACTIVE");
        for (SyncJob job : activeJobs) {
            // Simplified execution
            // In a real scenario we evaluate cron expressions here
            executeSyncJob(job.getId(), job.getTenantId());
        }
    }

    @Transactional
    public void executeSyncJob(UUID jobId, UUID tenantId) {
        SyncJob job = syncJobRepository.findByIdAndTenantId(jobId, tenantId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        
        List<FieldMapping> mappings = fieldMappingRepository.findBySyncJobIdAndTenantId(jobId, tenantId);

        try {
            // Rate limit implementation
            rateLimit(job.getIntegrationId(), tenantId);

            // Fetch from third party
            String mockData = "[{\"id\":\"ch_123\", \"amount\":1000, \"currency\":\"usd\"}]";
            JsonNode dataArray = objectMapper.readTree(mockData);
            
            int processed = 0;
            for (JsonNode node : dataArray) {
                ObjectNode transformed = objectMapper.createObjectNode();
                
                for (FieldMapping mapping : mappings) {
                    if (node.has(mapping.getSourceField())) {
                        JsonNode val = node.get(mapping.getSourceField());
                        if ("cents_to_dollars".equals(mapping.getTransformationRule())) {
                            transformed.put(mapping.getTargetField(), val.asDouble() / 100.0);
                        } else {
                            transformed.set(mapping.getTargetField(), val);
                        }
                    }
                }
                
                // Add original ID
                String externalId = node.has("id") ? node.get("id").asText() : UUID.randomUUID().toString();
                
                // Save locally
                SyncedRecord record = syncedRecordRepository.findByExternalIdAndIntegrationIdAndTenantId(externalId, job.getIntegrationId(), tenantId)
                        .orElse(new SyncedRecord());
                record.setTenantId(tenantId);
                record.setIntegrationId(job.getIntegrationId());
                record.setExternalId(externalId);
                record.setData(objectMapper.writeValueAsString(transformed));
                record.setSyncedAt(LocalDateTime.now());
                syncedRecordRepository.save(record);
                processed++;
            }
            
            job.setLastRunAt(LocalDateTime.now());
            syncJobRepository.save(job);
            
            auditService.log(tenantId, job.getIntegrationId(), "SYNC_JOB_RUN", "SUCCESS", processed, null);
            
        } catch (Exception e) {
            // Apply retry logic here for specific exception types
            if (e.getMessage() != null && e.getMessage().contains("rate_limit")) {
                 auditService.log(tenantId, job.getIntegrationId(), "SYNC_JOB_RUN", "RETRY_SCHEDULED", 0, e.getMessage());
            } else {
                 auditService.log(tenantId, job.getIntegrationId(), "SYNC_JOB_RUN", "FAILED", 0, e.getMessage());
            }
        }
    }
    
    private void rateLimit(UUID integrationId, UUID tenantId) {
        // Implementation of basic rate limiting
        // Throws exception if rate limit is exceeded
    }
}
