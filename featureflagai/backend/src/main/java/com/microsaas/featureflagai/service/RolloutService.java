package com.microsaas.featureflagai.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.featureflagai.domain.FeatureFlag;
import com.microsaas.featureflagai.domain.RolloutMetrics;
import com.microsaas.featureflagai.repository.FeatureFlagRepository;
import com.microsaas.featureflagai.repository.RolloutMetricsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RolloutService {

    private final FeatureFlagRepository flagRepository;
    private final RolloutMetricsRepository metricsRepository;

    private static final double ERROR_RATE_THRESHOLD = 0.05; // 5%

    public void controlRollout(UUID flagId, int newPct, boolean enabled) {
        UUID tenantId = TenantContext.require();
        Optional<FeatureFlag> flagOpt = flagRepository.findByIdAndTenantId(flagId, tenantId);

        if (flagOpt.isPresent()) {
            FeatureFlag flag = flagOpt.get();
            flag.setRolloutPct(newPct);
            flag.setEnabled(enabled);
            flagRepository.save(flag);
            log.info("Updated rollout for flag {}: enabled={}, pct={}", flagId, enabled, newPct);
        }
    }

    public void recordMetricsAndCheckAutoPause(UUID flagId, double errorRate) {
        UUID tenantId = TenantContext.require();

        RolloutMetrics metrics = RolloutMetrics.builder()
                .tenantId(tenantId)
                .flagId(flagId)
                .errorRate(errorRate)
                .build();
        metricsRepository.save(metrics);

        if (errorRate > ERROR_RATE_THRESHOLD) {
            log.warn("Error rate {} exceeds threshold {} for flag {}, auto-pausing rollout", errorRate, ERROR_RATE_THRESHOLD, flagId);
            controlRollout(flagId, 0, false);
        }
    }
}
