package com.microsaas.policyforge.service;

import com.microsaas.policyforge.domain.Policy;
import com.microsaas.policyforge.domain.PolicyStatus;
import com.microsaas.policyforge.domain.PolicyVersion;
import com.microsaas.policyforge.repository.PolicyRepository;
import com.microsaas.policyforge.repository.PolicyVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PolicyService {
    private final PolicyRepository policyRepository;
    private final PolicyVersionRepository policyVersionRepository;

    @Transactional
    public Policy createPolicy(String title, String content, String department, String owner, UUID tenantId) {
        Instant now = Instant.now();
        Policy policy = Policy.builder()
                .id(UUID.randomUUID())
                .title(title)
                .content(content)
                .department(department)
                .owner(owner)
                .version(1)
                .status(PolicyStatus.DRAFT)
                .tenantId(tenantId)
                .createdAt(now)
                .updatedAt(now)
                .build();
        
        policy = policyRepository.save(policy);

        PolicyVersion policyVersion = PolicyVersion.builder()
                .id(UUID.randomUUID())
                .policyId(policy.getId())
                .version(policy.getVersion())
                .content(content)
                .changedBy(owner)
                .changedAt(now)
                .changeSummary("Initial version")
                .tenantId(tenantId)
                .build();
        
        policyVersionRepository.save(policyVersion);

        return policy;
    }

    @Transactional
    public Policy updatePolicy(UUID id, String newContent, String changedBy, String changeSummary, UUID tenantId) {
        Policy policy = policyRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Policy not found"));
        
        Instant now = Instant.now();
        policy.setContent(newContent);
        policy.setVersion(policy.getVersion() + 1);
        policy.setUpdatedAt(now);

        policy = policyRepository.save(policy);

        PolicyVersion policyVersion = PolicyVersion.builder()
                .id(UUID.randomUUID())
                .policyId(policy.getId())
                .version(policy.getVersion())
                .content(newContent)
                .changedBy(changedBy)
                .changedAt(now)
                .changeSummary(changeSummary)
                .tenantId(tenantId)
                .build();

        policyVersionRepository.save(policyVersion);

        return policy;
    }

    @Transactional
    public Policy activatePolicy(UUID id, UUID tenantId) {
        Policy policy = policyRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Policy not found"));
        
        policy.setStatus(PolicyStatus.ACTIVE);
        policy.setUpdatedAt(Instant.now());
        return policyRepository.save(policy);
    }

    @Transactional(readOnly = true)
    public List<Policy> listPolicies(UUID tenantId) {
        return policyRepository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public Policy getPolicy(UUID id, UUID tenantId) {
        return policyRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Policy not found"));
    }
}
