package com.ecosystemmap.service;

import com.ecosystemmap.domain.IntegrationOpportunity;
import com.ecosystemmap.dto.EcosystemMapDto;
import com.ecosystemmap.repository.IntegrationOpportunityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service

public class AiInsightsService {

    private IntegrationOpportunityRepository integrationOpportunityRepository;
    private EcosystemService ecosystemService;

    public List<IntegrationOpportunity> getOpportunities(UUID tenantId) {
        return integrationOpportunityRepository.findByTenantId(tenantId);
    }

    @Transactional
    public List<IntegrationOpportunity> analyzeEcosystem(UUID tenantId) {
        // Fetch current ecosystem state
        EcosystemMapDto ecosystemMap = ecosystemService.getEcosystemMap(tenantId);
        
        // Simulating an AI call via LiteLLM using a mock response based on the ecosystem
        // (In a real system this would make an HTTP call to the LiteLLM endpoint)
        
        IntegrationOpportunity opp = new IntegrationOpportunity();
        opp.setId(UUID.randomUUID());
        opp.setTenantId(tenantId);
        opp.setSourceApp("App A");
        opp.setTargetApp("App B");
        opp.setDescription("Connecting App A to App B could automate data entry.");
        opp.setPotentialValue("Saves 10 hours/week");
        opp.setCreatedAt(LocalDateTime.now());
        opp.setUpdatedAt(LocalDateTime.now());
        
        integrationOpportunityRepository.save(opp);
        
        return integrationOpportunityRepository.findByTenantId(tenantId);
    }
}
