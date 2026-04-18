package com.microsaas.insuranceai.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.insuranceai.domain.Policy;
import com.microsaas.insuranceai.dto.RiskScoreResult;
import com.microsaas.insuranceai.repository.PolicyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

@Service
public class PolicyService {

    private final PolicyRepository policyRepository;
    private final AIRiskService aiRiskService;
    private final ApplicationEventPublisher eventPublisher;

    public PolicyService(PolicyRepository policyRepository, AIRiskService aiRiskService, ApplicationEventPublisher eventPublisher) {
        this.policyRepository = policyRepository;
        this.aiRiskService = aiRiskService;
        this.eventPublisher = eventPublisher;
    }

    public List<Policy> getAllPolicies() {
        UUID tenantId = UUID.fromString(TenantContext.require().toString());
        return policyRepository.findByTenantId(tenantId);
    }

    public Policy getPolicy(UUID id) {
        UUID tenantId = UUID.fromString(TenantContext.require().toString());
        return policyRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("Policy not found"));
    }

    @Transactional
    public Policy createPolicy(Policy policy) {
        UUID tenantId = UUID.fromString(TenantContext.require().toString());
        policy.setId(UUID.randomUUID());
        policy.setTenantId(tenantId);
        policy.setCreatedAt(LocalDateTime.now());
        policy.setUpdatedAt(LocalDateTime.now());

        // Trigger AI risk analysis
        RiskScoreResult riskScoreResult = aiRiskService.analyzePolicy(policy);
        policy.setAiRiskScore(riskScoreResult.getScore());
        policy.setAiRiskFactors(riskScoreResult.getFactors());

        Policy savedPolicy = policyRepository.save(policy);
        
        // Emit event
        Map<String, Object> event = new HashMap<>();
        event.put("type", "policy.created");
        event.put("policyId", savedPolicy.getId());
        event.put("tenantId", savedPolicy.getTenantId());
        eventPublisher.publishEvent(event);
        
        return savedPolicy;
    }
}
