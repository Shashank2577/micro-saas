package com.microsaas.taxoptimizer.service;

import com.microsaas.taxoptimizer.domain.entity.TaxProfile;
import com.microsaas.taxoptimizer.domain.repository.TaxProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaxProfileService {

    private final TaxProfileRepository taxProfileRepository;

    @Transactional
    public TaxProfile createOrUpdateProfile(UUID tenantId, UUID userId, TaxProfile profileData) {
        TaxProfile profile = taxProfileRepository.findByTenantIdAndUserId(tenantId, userId)
                .orElse(TaxProfile.builder()
                        .tenantId(tenantId)
                        .userId(userId)
                        .build());

        profile.setFilingStatus(profileData.getFilingStatus());
        profile.setDependentsCount(profileData.getDependentsCount());
        profile.setStateResidence(profileData.getStateResidence());
        profile.setBusinessType(profileData.getBusinessType());

        return taxProfileRepository.save(profile);
    }

    @Transactional(readOnly = true)
    public Optional<TaxProfile> getProfile(UUID tenantId, UUID userId) {
        return taxProfileRepository.findByTenantIdAndUserId(tenantId, userId);
    }
}
