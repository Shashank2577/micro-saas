package com.crosscutting.starter.flags;

import com.crosscutting.starter.error.CcErrorCodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class FeatureFlagService {

    private static final Logger log = LoggerFactory.getLogger(FeatureFlagService.class);
    private static final Duration CACHE_TTL = Duration.ofMinutes(5);
    private static final String CACHE_PREFIX = "flags:";

    private final FeatureFlagRepository flagRepository;
    private final FlagOverrideRepository overrideRepository;
    private final StringRedisTemplate redisTemplate;

    public FeatureFlagService(FeatureFlagRepository flagRepository,
                              FlagOverrideRepository overrideRepository,
                              StringRedisTemplate redisTemplate) {
        this.flagRepository = flagRepository;
        this.overrideRepository = overrideRepository;
        this.redisTemplate = redisTemplate;
    }

    /**
     * Evaluate whether a flag is enabled using the override hierarchy:
     * user override > tenant override > global override > default value.
     * Results are cached in Redis with a 5-minute TTL.
     * If the flag does not exist, it is auto-created with defaultEnabled = false.
     */
    public boolean isEnabled(String flagKey, UUID tenantId, UUID userId) {
        return isEnabled(flagKey, tenantId, userId, false);
    }

    /**
     * Evaluate whether a flag is enabled, using the provided default value
     * if the flag does not yet exist in the database.
     * Auto-creates the flag on first access so it becomes visible in the admin UI.
     */
    public boolean isEnabled(String flagKey, UUID tenantId, UUID userId, boolean defaultValue) {
        String cacheKey = buildCacheKey(flagKey, tenantId, userId);

        // Check Redis cache first
        String cached = redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            return Boolean.parseBoolean(cached);
        }

        FeatureFlag flag = flagRepository.findByKey(flagKey)
                .orElseGet(() -> {
                    // Auto-create the flag with the provided default
                    log.info("Auto-creating feature flag '{}' with defaultEnabled={}", flagKey, defaultValue);
                    FeatureFlag newFlag = new FeatureFlag();
                    newFlag.setKey(flagKey);
                    newFlag.setDescription("Auto-created on first access");
                    newFlag.setDefaultEnabled(defaultValue);
                    return flagRepository.save(newFlag);
                });

        boolean result = evaluateFlag(flag, tenantId, userId);

        // Cache result in Redis
        redisTemplate.opsForValue().set(cacheKey, String.valueOf(result), CACHE_TTL);

        return result;
    }

    private boolean evaluateFlag(FeatureFlag flag, UUID tenantId, UUID userId) {
        // 1. User-level override (most specific)
        if (tenantId != null && userId != null) {
            Optional<FlagOverride> userOverride =
                    overrideRepository.findByFlagIdAndTenantIdAndUserId(flag.getId(), tenantId, userId);
            if (userOverride.isPresent()) {
                return userOverride.get().isEnabled();
            }
        }

        // 2. Tenant-level override
        if (tenantId != null) {
            Optional<FlagOverride> tenantOverride =
                    overrideRepository.findByFlagIdAndTenantIdAndUserIdIsNull(flag.getId(), tenantId);
            if (tenantOverride.isPresent()) {
                return tenantOverride.get().isEnabled();
            }
        }

        // 3. Global override (no tenant, no user)
        Optional<FlagOverride> globalOverride =
                overrideRepository.findByFlagIdAndTenantIdIsNullAndUserIdIsNull(flag.getId());
        if (globalOverride.isPresent()) {
            return globalOverride.get().isEnabled();
        }

        // 4. Default value
        return flag.isDefaultEnabled();
    }

    /**
     * Set an override for a flag. Pass null for tenantId/userId for global overrides.
     */
    @Transactional
    public FlagOverride setOverride(String flagKey, UUID tenantId, UUID userId, boolean enabled) {
        FeatureFlag flag = flagRepository.findByKey(flagKey)
                .orElseThrow(() -> CcErrorCodes.resourceNotFound("Feature flag not found: " + flagKey));

        // Find existing override or create new one
        Optional<FlagOverride> existing;
        if (tenantId != null && userId != null) {
            existing = overrideRepository.findByFlagIdAndTenantIdAndUserId(flag.getId(), tenantId, userId);
        } else if (tenantId != null) {
            existing = overrideRepository.findByFlagIdAndTenantIdAndUserIdIsNull(flag.getId(), tenantId);
        } else {
            existing = overrideRepository.findByFlagIdAndTenantIdIsNullAndUserIdIsNull(flag.getId());
        }

        FlagOverride override;
        if (existing.isPresent()) {
            override = existing.get();
            override.setEnabled(enabled);
        } else {
            override = new FlagOverride();
            override.setFlagId(flag.getId());
            override.setTenantId(tenantId);
            override.setUserId(userId);
            override.setEnabled(enabled);
        }

        override = overrideRepository.save(override);

        // Invalidate cache for this flag evaluation
        invalidateCache(flagKey, tenantId, userId);

        log.info("Set override for flag '{}': enabled={} (tenant={}, user={})", flagKey, enabled, tenantId, userId);
        return override;
    }

    /**
     * Create a new feature flag.
     */
    @Transactional
    public FeatureFlag createFlag(String key, String description, boolean defaultEnabled) {
        // Check for duplicate key
        if (flagRepository.findByKey(key).isPresent()) {
            throw CcErrorCodes.resourceConflict("Feature flag already exists: " + key);
        }

        FeatureFlag flag = new FeatureFlag();
        flag.setKey(key);
        flag.setDescription(description);
        flag.setDefaultEnabled(defaultEnabled);
        flag = flagRepository.save(flag);

        log.info("Created feature flag '{}' (id={}, defaultEnabled={})", key, flag.getId(), defaultEnabled);
        return flag;
    }

    /**
     * List all defined feature flags.
     */
    public List<FeatureFlag> listFlags() {
        return flagRepository.findAll();
    }

    /**
     * Evaluate all flags for a given tenant and user context.
     */
    public Map<String, Boolean> evaluateAll(UUID tenantId, UUID userId) {
        List<FeatureFlag> flags = flagRepository.findAll();
        Map<String, Boolean> results = new LinkedHashMap<>();
        for (FeatureFlag flag : flags) {
            String cacheKey = buildCacheKey(flag.getKey(), tenantId, userId);
            String cached = redisTemplate.opsForValue().get(cacheKey);
            if (cached != null) {
                results.put(flag.getKey(), Boolean.parseBoolean(cached));
            } else {
                boolean result = evaluateFlag(flag, tenantId, userId);
                redisTemplate.opsForValue().set(cacheKey, String.valueOf(result), CACHE_TTL);
                results.put(flag.getKey(), result);
            }
        }
        return results;
    }

    private String buildCacheKey(String flagKey, UUID tenantId, UUID userId) {
        return CACHE_PREFIX + flagKey + ":" + tenantId + ":" + userId;
    }

    private void invalidateCache(String flagKey, UUID tenantId, UUID userId) {
        String cacheKey = buildCacheKey(flagKey, tenantId, userId);
        redisTemplate.delete(cacheKey);
        log.debug("Invalidated flag cache for key '{}'", cacheKey);
    }
}
