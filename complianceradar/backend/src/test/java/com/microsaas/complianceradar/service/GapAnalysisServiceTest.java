package com.microsaas.complianceradar.service;

import com.microsaas.complianceradar.domain.ComplianceGap;
import com.microsaas.complianceradar.domain.CompliancePolicy;
import com.microsaas.complianceradar.domain.RegulatoryChange;
import com.microsaas.complianceradar.repository.ComplianceGapRepository;
import com.microsaas.complianceradar.repository.CompliancePolicyRepository;
import com.microsaas.complianceradar.repository.RegulatoryChangeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class GapAnalysisServiceTest {

    @Mock
    private RegulatoryChangeRepository regulatoryChangeRepository;

    @Mock
    private CompliancePolicyRepository compliancePolicyRepository;

    @Mock
    private ComplianceGapRepository complianceGapRepository;

    @InjectMocks
    private GapAnalysisService gapAnalysisService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void analyzeGaps_createsGapsForMatchingPolicies() {
        UUID tenantId = UUID.randomUUID();
        UUID changeId = UUID.randomUUID();
        UUID policyId = UUID.randomUUID();

        RegulatoryChange change = RegulatoryChange.builder()
                .id(changeId)
                .tenantId(tenantId)
                .regulationName("GDPR Update")
                .summary("Data privacy and protection rules")
                .build();

        CompliancePolicy policy = CompliancePolicy.builder()
                .id(policyId)
                .tenantId(tenantId)
                .title("Privacy Policy")
                .content("This policy covers data privacy for our users.")
                .build();

        when(regulatoryChangeRepository.findByIdAndTenantId(changeId, tenantId)).thenReturn(Optional.of(change));
        when(compliancePolicyRepository.findByTenantId(tenantId)).thenReturn(List.of(policy));
        when(complianceGapRepository.save(any(ComplianceGap.class))).thenAnswer(i -> i.getArguments()[0]);

        List<ComplianceGap> gaps = gapAnalysisService.analyzeGaps(changeId, tenantId);

        assertEquals(1, gaps.size());
        assertEquals(changeId, gaps.get(0).getRegulatoryChangeId());
        assertEquals(policyId, gaps.get(0).getPolicyId());
    }

    @Test
    void analyzeGaps_noGapsWhenNoPolicyMatch() {
        UUID tenantId = UUID.randomUUID();
        UUID changeId = UUID.randomUUID();
        UUID policyId = UUID.randomUUID();

        RegulatoryChange change = RegulatoryChange.builder()
                .id(changeId)
                .tenantId(tenantId)
                .regulationName("GDPR Update")
                .summary("Data protection rules")
                .build();

        CompliancePolicy policy = CompliancePolicy.builder()
                .id(policyId)
                .tenantId(tenantId)
                .title("Security Policy")
                .content("Network firewall guidelines.")
                .build();

        when(regulatoryChangeRepository.findByIdAndTenantId(changeId, tenantId)).thenReturn(Optional.of(change));
        when(compliancePolicyRepository.findByTenantId(tenantId)).thenReturn(List.of(policy));

        List<ComplianceGap> gaps = gapAnalysisService.analyzeGaps(changeId, tenantId);

        assertEquals(0, gaps.size());
    }
}
