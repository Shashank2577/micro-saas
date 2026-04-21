package com.microsaas.featureflagai.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.featureflagai.domain.FeatureFlag;
import com.microsaas.featureflagai.repository.FeatureFlagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlagCleanupService {

    private final FeatureFlagRepository flagRepository;

    public List<FeatureFlag> getCleanupSuggestions() {
        UUID tenantId = TenantContext.require();
        List<FeatureFlag> allFlags = flagRepository.findByTenantId(tenantId);

        OffsetDateTime threshold = OffsetDateTime.now().minusDays(90);

        return allFlags.stream()
                .filter(flag -> flag.getUpdatedAt().isBefore(threshold))
                .collect(Collectors.toList());
    }
}
