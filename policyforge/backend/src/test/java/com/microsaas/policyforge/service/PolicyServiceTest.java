package com.microsaas.policyforge.service;

import com.microsaas.policyforge.domain.Policy;
import com.microsaas.policyforge.domain.PolicyStatus;
import com.microsaas.policyforge.domain.PolicyVersion;
import com.microsaas.policyforge.repository.PolicyRepository;
import com.microsaas.policyforge.repository.PolicyVersionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PolicyServiceTest {

    @Mock
    private PolicyRepository policyRepository;

    @Mock
    private PolicyVersionRepository policyVersionRepository;

    @InjectMocks
    private PolicyService policyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPolicy_storesWithVersion1() {
        UUID tenantId = UUID.randomUUID();
        
        when(policyRepository.save(any(Policy.class))).thenAnswer(i -> i.getArguments()[0]);
        when(policyVersionRepository.save(any(PolicyVersion.class))).thenAnswer(i -> i.getArguments()[0]);

        Policy policy = policyService.createPolicy("Code of Conduct", "Be nice", "HR", "Alice", tenantId);

        assertEquals("Code of Conduct", policy.getTitle());
        assertEquals("Be nice", policy.getContent());
        assertEquals(1, policy.getVersion());
        assertEquals(PolicyStatus.DRAFT, policy.getStatus());

        ArgumentCaptor<PolicyVersion> versionCaptor = ArgumentCaptor.forClass(PolicyVersion.class);
        verify(policyVersionRepository).save(versionCaptor.capture());
        
        PolicyVersion savedVersion = versionCaptor.getValue();
        assertEquals(1, savedVersion.getVersion());
        assertEquals("Be nice", savedVersion.getContent());
        assertEquals("Initial version", savedVersion.getChangeSummary());
    }

    @Test
    void updatePolicy_incrementsVersionAndSavesHistory() {
        UUID tenantId = UUID.randomUUID();
        UUID policyId = UUID.randomUUID();
        
        Policy existingPolicy = Policy.builder()
                .id(policyId)
                .title("Code of Conduct")
                .content("Be nice")
                .version(1)
                .tenantId(tenantId)
                .status(PolicyStatus.DRAFT)
                .build();
                
        when(policyRepository.findByIdAndTenantId(policyId, tenantId)).thenReturn(Optional.of(existingPolicy));
        when(policyRepository.save(any(Policy.class))).thenAnswer(i -> i.getArguments()[0]);
        when(policyVersionRepository.save(any(PolicyVersion.class))).thenAnswer(i -> i.getArguments()[0]);

        Policy updatedPolicy = policyService.updatePolicy(policyId, "Be very nice", "Bob", "Updated rules", tenantId);

        assertEquals("Be very nice", updatedPolicy.getContent());
        assertEquals(2, updatedPolicy.getVersion());

        ArgumentCaptor<PolicyVersion> versionCaptor = ArgumentCaptor.forClass(PolicyVersion.class);
        verify(policyVersionRepository).save(versionCaptor.capture());
        
        PolicyVersion savedVersion = versionCaptor.getValue();
        assertEquals(2, savedVersion.getVersion());
        assertEquals("Be very nice", savedVersion.getContent());
        assertEquals("Updated rules", savedVersion.getChangeSummary());
        assertEquals("Bob", savedVersion.getChangedBy());
    }

    @Test
    void activatePolicy_changesStatusToACTIVE() {
        UUID tenantId = UUID.randomUUID();
        UUID policyId = UUID.randomUUID();
        
        Policy existingPolicy = Policy.builder()
                .id(policyId)
                .title("Code of Conduct")
                .status(PolicyStatus.DRAFT)
                .tenantId(tenantId)
                .build();

        when(policyRepository.findByIdAndTenantId(policyId, tenantId)).thenReturn(Optional.of(existingPolicy));
        when(policyRepository.save(any(Policy.class))).thenAnswer(i -> i.getArguments()[0]);

        Policy activatedPolicy = policyService.activatePolicy(policyId, tenantId);

        assertEquals(PolicyStatus.ACTIVE, activatedPolicy.getStatus());
    }
}
