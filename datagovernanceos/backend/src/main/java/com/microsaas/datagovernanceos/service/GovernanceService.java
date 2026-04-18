package com.microsaas.datagovernanceos.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.datagovernanceos.dto.DataClassificationResult;
import com.microsaas.datagovernanceos.entity.ComplianceAudit;
import com.microsaas.datagovernanceos.entity.DataAsset;
import com.microsaas.datagovernanceos.entity.GovernancePolicy;
import com.microsaas.datagovernanceos.repository.ComplianceAuditRepository;
import com.microsaas.datagovernanceos.repository.DataAssetRepository;
import com.microsaas.datagovernanceos.repository.GovernancePolicyRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class GovernanceService {

    private final DataAssetRepository dataAssetRepository;
    private final GovernancePolicyRepository policyRepository;
    private final ComplianceAuditRepository auditRepository;
    private final DataClassificationAiService aiService;
    private final ApplicationEventPublisher eventPublisher;

    public GovernanceService(DataAssetRepository dataAssetRepository,
                             GovernancePolicyRepository policyRepository,
                             ComplianceAuditRepository auditRepository,
                             DataClassificationAiService aiService,
                             ApplicationEventPublisher eventPublisher) {
        this.dataAssetRepository = dataAssetRepository;
        this.policyRepository = policyRepository;
        this.auditRepository = auditRepository;
        this.aiService = aiService;
        this.eventPublisher = eventPublisher;
    }

    public List<DataAsset> listAssets() {
        return dataAssetRepository.findByTenantId(TenantContext.require());
    }

    public DataAsset getAsset(UUID id) {
        return dataAssetRepository.findByIdAndTenantId(id, TenantContext.require()).orElseThrow();
    }

    public DataAsset createAsset(DataAsset asset) {
        asset.setTenantId(TenantContext.require());
        
        // Auto-classify using AI if missing
        if (asset.getClassification() == null || asset.getPiiStatus() == null) {
            DataClassificationResult classification = aiService.classifyAsset(asset);
            asset.setClassification(classification.getClassification());
            asset.setPiiStatus(classification.getPiiStatus());
        }

        DataAsset saved = dataAssetRepository.save(asset);
        // Emitting spring event
        eventPublisher.publishEvent(Map.of("eventType", "data_asset.created", "assetId", saved.getId()));
        return saved;
    }

    public DataAsset updateAsset(UUID id, DataAsset assetUpdates) {
        DataAsset existing = getAsset(id);
        existing.setName(assetUpdates.getName());
        existing.setType(assetUpdates.getType());
        existing.setDescription(assetUpdates.getDescription());
        existing.setClassification(assetUpdates.getClassification());
        existing.setPiiStatus(assetUpdates.getPiiStatus());
        existing.setOwner(assetUpdates.getOwner());
        return dataAssetRepository.save(existing);
    }

    public void deleteAsset(UUID id) {
        DataAsset existing = getAsset(id);
        dataAssetRepository.delete(existing);
    }

    public List<GovernancePolicy> listPolicies() {
        return policyRepository.findByTenantId(TenantContext.require());
    }

    public GovernancePolicy getPolicy(UUID id) {
        return policyRepository.findByIdAndTenantId(id, TenantContext.require()).orElseThrow();
    }

    public GovernancePolicy createPolicy(GovernancePolicy policy) {
        policy.setTenantId(TenantContext.require());
        if (policy.getStatus() == null) {
            policy.setStatus("ACTIVE");
        }
        GovernancePolicy saved = policyRepository.save(policy);
        eventPublisher.publishEvent(Map.of("eventType", "policy.created", "policyId", saved.getId()));
        return saved;
    }
    
    public GovernancePolicy updatePolicy(UUID id, GovernancePolicy policyUpdates) {
        GovernancePolicy existing = getPolicy(id);
        existing.setName(policyUpdates.getName());
        existing.setDescription(policyUpdates.getDescription());
        existing.setRuleDefinition(policyUpdates.getRuleDefinition());
        existing.setEnforcementLevel(policyUpdates.getEnforcementLevel());
        existing.setStatus(policyUpdates.getStatus());
        return policyRepository.save(existing);
    }

    public List<ComplianceAudit> listAudits() {
        return auditRepository.findByTenantId(TenantContext.require());
    }

    public void runAudits(UUID assetId) {
        UUID tenantId = TenantContext.require();
        DataAsset asset = getAsset(assetId);
        List<GovernancePolicy> policies = policyRepository.findByTenantIdAndStatus(tenantId, "ACTIVE");

        for (GovernancePolicy policy : policies) {
            ComplianceAudit audit = new ComplianceAudit();
            audit.setTenantId(tenantId);
            audit.setAssetId(assetId);
            audit.setPolicyId(policy.getId());
            
            // Dummy logic for evaluation: If asset has PII and policy name contains PII, audit failed.
            if (Boolean.TRUE.equals(asset.getPiiStatus()) && policy.getName().toLowerCase().contains("pii")) {
                audit.setStatus("FAILED");
                audit.setFindings("Asset contains PII violating the policy rules.");
                eventPublisher.publishEvent(Map.of("eventType", "audit.failed", "auditId", audit.getId()));
            } else {
                audit.setStatus("PASSED");
                audit.setFindings("Asset meets policy requirements.");
            }
            
            auditRepository.save(audit);
        }
    }
}
