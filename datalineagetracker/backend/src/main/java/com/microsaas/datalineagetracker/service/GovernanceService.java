package com.microsaas.datalineagetracker.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.datalineagetracker.dto.PolicyDto;
import com.microsaas.datalineagetracker.entity.GovernancePolicy;
import com.microsaas.datalineagetracker.repository.GovernancePolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GovernanceService {
    private final GovernancePolicyRepository policyRepository;

    @Transactional(readOnly = true)
    public List<GovernancePolicy> getAllPolicies() {
        return policyRepository.findAllByTenantId(TenantContext.require());
    }

    @Transactional
    public GovernancePolicy createPolicy(PolicyDto dto) {
        GovernancePolicy policy = new GovernancePolicy();
        policy.setTenantId(TenantContext.require());
        policy.setName(dto.getName());
        policy.setDescription(dto.getDescription());
        policy.setPolicyType(dto.getPolicyType());
        policy.setRules(dto.getRules());
        if (dto.getIsActive() != null) {
            policy.setIsActive(dto.getIsActive());
        }
        return policyRepository.save(policy);
    }
}
