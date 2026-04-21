package com.micro_saas.featureflagai.service;

import com.micro_saas.featureflagai.domain.FeatureFlag;
import com.micro_saas.featureflagai.repository.FeatureFlagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class FeatureFlagService {

    private final FeatureFlagRepository featureFlagRepository;

    public FeatureFlagService(FeatureFlagRepository featureFlagRepository) {
        this.featureFlagRepository = featureFlagRepository;
    }

    public FeatureFlag createFlag(FeatureFlag flag) {
        if (flag.getTenantId() == null) {
            flag.setTenantId(UUID.randomUUID()); // Set a dummy tenantId if not provided
        }
        return featureFlagRepository.save(flag);
    }

    @Transactional(readOnly = true)
    public FeatureFlag getFlag(UUID id) {
        return featureFlagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flag not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<FeatureFlag> getAllFlags() {
        return featureFlagRepository.findAll();
    }

    public FeatureFlag updateFlag(UUID id, FeatureFlag flagUpdates) {
        FeatureFlag existingFlag = getFlag(id);
        existingFlag.setName(flagUpdates.getName());
        existingFlag.setEnabled(flagUpdates.isEnabled());
        return featureFlagRepository.save(existingFlag);
    }

    public void deleteFlag(UUID id) {
        featureFlagRepository.deleteById(id);
    }
}