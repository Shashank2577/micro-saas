package com.microsaas.complianceradar.service;

import com.microsaas.complianceradar.domain.*;
import com.microsaas.complianceradar.repository.ComplianceGapRepository;
import com.microsaas.complianceradar.repository.CompliancePolicyRepository;
import com.microsaas.complianceradar.repository.RegulatoryChangeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GapAnalysisService {
    private final RegulatoryChangeRepository regulatoryChangeRepository;
    private final CompliancePolicyRepository compliancePolicyRepository;
    private final ComplianceGapRepository complianceGapRepository;
    // We would inject AiService here normally, but gap analysis by keyword is requested.
    // The instruction says "matches change to policies by keyword" for GapAnalysisService
    // I will check if AI service should be used. The instruction says: "GapAnalysisService.java: analyzeGaps(regulatoryChangeId, tenantId) — matches change to policies by keyword, creates ComplianceGap records"

    @Transactional
    public List<ComplianceGap> analyzeGaps(UUID regulatoryChangeId, UUID tenantId) {
        RegulatoryChange change = regulatoryChangeRepository.findByIdAndTenantId(regulatoryChangeId, tenantId)
                .orElseThrow(() -> new RuntimeException("Regulatory Change not found"));

        List<CompliancePolicy> policies = compliancePolicyRepository.findByTenantId(tenantId);
        List<ComplianceGap> newGaps = new ArrayList<>();

        String[] keywords = change.getSummary().toLowerCase().split("\\s+");

        for (CompliancePolicy policy : policies) {
            boolean matches = false;
            String content = policy.getContent().toLowerCase();
            for (String keyword : keywords) {
                if (keyword.length() > 3 && content.contains(keyword)) {
                    matches = true;
                    break;
                }
            }

            if (matches) {
                ComplianceGap gap = ComplianceGap.builder()
                        .regulatoryChangeId(change.getId())
                        .policyId(policy.getId())
                        .gapDescription("Potential gap identified for policy " + policy.getTitle() + " against regulation " + change.getRegulationName())
                        .severity(Severity.MEDIUM) // Default severity
                        .status(GapStatus.OPEN)
                        .owner(policy.getOwner())
                        .tenantId(tenantId)
                        .build();
                newGaps.add(complianceGapRepository.save(gap));
            }
        }

        return newGaps;
    }
}
