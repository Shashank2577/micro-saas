package com.microsaas.policyforge.service;

import com.microsaas.policyforge.domain.PolicyAcknowledgment;
import com.microsaas.policyforge.repository.PolicyAcknowledgmentRepository;
import com.microsaas.policyforge.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AcknowledgmentService {
    private final PolicyAcknowledgmentRepository acknowledgmentRepository;
    private final PolicyRepository policyRepository;

    @Transactional
    public PolicyAcknowledgment acknowledge(UUID policyId, String userId, UUID tenantId) {
        if (policyRepository.findByIdAndTenantId(policyId, tenantId).isEmpty()) {
            throw new IllegalArgumentException("Policy not found");
        }

        PolicyAcknowledgment ack = PolicyAcknowledgment.builder()
                .id(UUID.randomUUID())
                .policyId(policyId)
                .userId(userId)
                .acknowledgedAt(Instant.now())
                .tenantId(tenantId)
                .build();
        
        return acknowledgmentRepository.save(ack);
    }

    @Transactional(readOnly = true)
    public List<PolicyAcknowledgment> getAcknowledgments(UUID policyId, UUID tenantId) {
        return acknowledgmentRepository.findByPolicyIdAndTenantId(policyId, tenantId);
    }

    @Transactional(readOnly = true)
    public double getCompletionRate(UUID policyId, UUID tenantId) {
        // Here we'd ideally divide by the total number of users who need to acknowledge,
        // but for now we'll just return the total count as a placeholder double or assume a hardcoded total
        long count = acknowledgmentRepository.countByPolicyIdAndTenantId(policyId, tenantId);
        // Assuming 100 total employees for now, adjust based on actual logic if there is a User repository
        return (double) count / 100.0;
    }
}
