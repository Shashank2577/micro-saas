package com.microsaas.insuranceai.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.insuranceai.domain.Claim;
import com.microsaas.insuranceai.dto.FraudScoreResult;
import com.microsaas.insuranceai.repository.ClaimRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

@Service
public class ClaimService {

    private final ClaimRepository claimRepository;
    private final AIFraudService aiFraudService;
    private final ApplicationEventPublisher eventPublisher;

    public ClaimService(ClaimRepository claimRepository, AIFraudService aiFraudService, ApplicationEventPublisher eventPublisher) {
        this.claimRepository = claimRepository;
        this.aiFraudService = aiFraudService;
        this.eventPublisher = eventPublisher;
    }

    public List<Claim> getAllClaims() {
        UUID tenantId = UUID.fromString(TenantContext.require().toString());
        return claimRepository.findByTenantId(tenantId);
    }

    public Claim getClaim(UUID id) {
        UUID tenantId = UUID.fromString(TenantContext.require().toString());
        return claimRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("Claim not found"));
    }

    @Transactional
    public Claim createClaim(Claim claim) {
        UUID tenantId = UUID.fromString(TenantContext.require().toString());
        claim.setId(UUID.randomUUID());
        claim.setTenantId(tenantId);
        claim.setCreatedAt(LocalDateTime.now());
        claim.setUpdatedAt(LocalDateTime.now());
        claim.setStatus("NEW");

        // Trigger AI fraud analysis
        FraudScoreResult fraudScoreResult = aiFraudService.analyzeClaim(claim);
        claim.setAiFraudScore(fraudScoreResult.getScore());
        claim.setAiFraudReasoning(fraudScoreResult.getReasoning());

        Claim savedClaim = claimRepository.save(claim);
        
        // Emit event
        Map<String, Object> event = new HashMap<>();
        event.put("type", "claim.created");
        event.put("claimId", savedClaim.getId());
        event.put("tenantId", savedClaim.getTenantId());
        eventPublisher.publishEvent(event);
        
        return savedClaim;
    }

    @Transactional
    public Claim updateStatus(UUID id, String status) {
        Claim claim = getClaim(id);
        claim.setStatus(status);
        claim.setUpdatedAt(LocalDateTime.now());
        Claim savedClaim = claimRepository.save(claim);
        
        // Emit event
        Map<String, Object> event = new HashMap<>();
        event.put("type", "claim.status_updated");
        event.put("claimId", savedClaim.getId());
        event.put("status", savedClaim.getStatus());
        event.put("tenantId", savedClaim.getTenantId());
        eventPublisher.publishEvent(event);
        
        return savedClaim;
    }
}
