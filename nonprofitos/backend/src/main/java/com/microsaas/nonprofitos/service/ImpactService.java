package com.microsaas.nonprofitos.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.nonprofitos.ai.AiService;
import com.microsaas.nonprofitos.domain.Impact;
import com.microsaas.nonprofitos.dto.ImpactDto;
import com.microsaas.nonprofitos.repository.ImpactRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ImpactService {

    private final ImpactRepository impactRepository;
    private final AiService aiService;

    public ImpactService(ImpactRepository impactRepository, AiService aiService) {
        this.impactRepository = impactRepository;
        this.aiService = aiService;
    }

    public List<Impact> getAllImpacts() {
        UUID tenantId = TenantContext.require();
        return impactRepository.findByTenantId(tenantId);
    }

    public Impact createImpact(ImpactDto dto) {
        UUID tenantId = TenantContext.require();
        Impact impact = new Impact();
        impact.setTenantId(tenantId);
        impact.setMetricName(dto.getMetricName());
        impact.setMetricValue(dto.getMetricValue());
        return impactRepository.save(impact);
    }

    public String generateNarrative() {
        UUID tenantId = TenantContext.require();
        List<Impact> impacts = impactRepository.findByTenantId(tenantId);
        
        String metricsString = impacts.stream()
            .map(i -> i.getMetricName() + ": " + i.getMetricValue())
            .collect(Collectors.joining(", "));

        String narrative = aiService.generateImpactNarrative(metricsString);
        
        // Save narrative to all recent impacts for simplicity in this demo
        impacts.forEach(i -> {
            i.setNarrative(narrative);
            impactRepository.save(i);
        });
        
        return narrative;
    }
}
