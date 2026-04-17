package com.microsaas.apigatekeeper.service;

import com.microsaas.apigatekeeper.entity.RateLimitPolicy;
import com.microsaas.apigatekeeper.repository.RateLimitPolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RateLimitPolicyService {

    private final RateLimitPolicyRepository repository;

    @Transactional
    public RateLimitPolicy createPolicy(String tenantId, RateLimitPolicy policy) {
        policy.setTenantId(tenantId);
        policy.setCreatedAt(ZonedDateTime.now());
        policy.setUpdatedAt(ZonedDateTime.now());
        return repository.save(policy);
    }

    public List<RateLimitPolicy> getPolicies(String tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Transactional
    public void deletePolicy(String tenantId, UUID id) {
        RateLimitPolicy policy = repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("Policy not found"));
        repository.delete(policy);
    }
}
