package com.crosscutting.starter.flags;

import com.crosscutting.starter.error.CcException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeatureFlagServiceTest {

    @Mock
    private FeatureFlagRepository flagRepository;

    @Mock
    private FlagOverrideRepository overrideRepository;

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private FeatureFlagService featureFlagService;

    private final UUID tenantId = UUID.randomUUID();
    private final UUID userId = UUID.randomUUID();

    private final UUID flagId1 = UUID.randomUUID();
    private final UUID flagId2 = UUID.randomUUID();

    private void stubRedisValueOps() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    private FeatureFlag createFlag(UUID id, String key, boolean defaultEnabled) {
        FeatureFlag flag = new FeatureFlag();
        flag.setId(id);
        flag.setKey(key);
        flag.setDescription("Test flag");
        flag.setDefaultEnabled(defaultEnabled);
        return flag;
    }

    // --- isEnabled hierarchy tests ---

    @Test
    void isEnabled_returnsUserOverrideWhenPresent() {
        stubRedisValueOps();
        FeatureFlag flag = createFlag(flagId1, "dark-mode", false);
        when(valueOperations.get(anyString())).thenReturn(null);
        when(flagRepository.findByKey("dark-mode")).thenReturn(Optional.of(flag));

        FlagOverride userOverride = new FlagOverride();
        userOverride.setEnabled(true);
        when(overrideRepository.findByFlagIdAndTenantIdAndUserId(flagId1, tenantId, userId))
                .thenReturn(Optional.of(userOverride));

        boolean result = featureFlagService.isEnabled("dark-mode", tenantId, userId);

        assertThat(result).isTrue();
    }

    @Test
    void isEnabled_returnsTenantOverrideWhenNoUserOverride() {
        stubRedisValueOps();
        FeatureFlag flag = createFlag(flagId1, "dark-mode", false);
        when(valueOperations.get(anyString())).thenReturn(null);
        when(flagRepository.findByKey("dark-mode")).thenReturn(Optional.of(flag));

        when(overrideRepository.findByFlagIdAndTenantIdAndUserId(flagId1, tenantId, userId))
                .thenReturn(Optional.empty());

        FlagOverride tenantOverride = new FlagOverride();
        tenantOverride.setEnabled(true);
        when(overrideRepository.findByFlagIdAndTenantIdAndUserIdIsNull(flagId1, tenantId))
                .thenReturn(Optional.of(tenantOverride));

        boolean result = featureFlagService.isEnabled("dark-mode", tenantId, userId);

        assertThat(result).isTrue();
    }

    @Test
    void isEnabled_returnsGlobalOverrideWhenNoTenantOrUserOverride() {
        stubRedisValueOps();
        FeatureFlag flag = createFlag(flagId1, "dark-mode", false);
        when(valueOperations.get(anyString())).thenReturn(null);
        when(flagRepository.findByKey("dark-mode")).thenReturn(Optional.of(flag));

        when(overrideRepository.findByFlagIdAndTenantIdAndUserId(flagId1, tenantId, userId))
                .thenReturn(Optional.empty());
        when(overrideRepository.findByFlagIdAndTenantIdAndUserIdIsNull(flagId1, tenantId))
                .thenReturn(Optional.empty());

        FlagOverride globalOverride = new FlagOverride();
        globalOverride.setEnabled(true);
        when(overrideRepository.findByFlagIdAndTenantIdIsNullAndUserIdIsNull(flagId1))
                .thenReturn(Optional.of(globalOverride));

        boolean result = featureFlagService.isEnabled("dark-mode", tenantId, userId);

        assertThat(result).isTrue();
    }

    @Test
    void isEnabled_returnsDefaultWhenNoOverrides() {
        stubRedisValueOps();
        FeatureFlag flag = createFlag(flagId1, "dark-mode", true);
        when(valueOperations.get(anyString())).thenReturn(null);
        when(flagRepository.findByKey("dark-mode")).thenReturn(Optional.of(flag));

        when(overrideRepository.findByFlagIdAndTenantIdAndUserId(flagId1, tenantId, userId))
                .thenReturn(Optional.empty());
        when(overrideRepository.findByFlagIdAndTenantIdAndUserIdIsNull(flagId1, tenantId))
                .thenReturn(Optional.empty());
        when(overrideRepository.findByFlagIdAndTenantIdIsNullAndUserIdIsNull(flagId1))
                .thenReturn(Optional.empty());

        boolean result = featureFlagService.isEnabled("dark-mode", tenantId, userId);

        assertThat(result).isTrue();
    }

    @Test
    void isEnabled_userOverrideTakesPrecedenceOverTenantOverride() {
        stubRedisValueOps();
        FeatureFlag flag = createFlag(flagId1, "beta-feature", true);
        when(valueOperations.get(anyString())).thenReturn(null);
        when(flagRepository.findByKey("beta-feature")).thenReturn(Optional.of(flag));

        // User override says disabled
        FlagOverride userOverride = new FlagOverride();
        userOverride.setEnabled(false);
        when(overrideRepository.findByFlagIdAndTenantIdAndUserId(flagId1, tenantId, userId))
                .thenReturn(Optional.of(userOverride));

        boolean result = featureFlagService.isEnabled("beta-feature", tenantId, userId);

        // User override (false) wins over default (true)
        assertThat(result).isFalse();
    }

    // --- Caching tests ---

    @Test
    void isEnabled_returnsCachedValueWhenPresent() {
        stubRedisValueOps();
        String cacheKey = "flags:dark-mode:" + tenantId + ":" + userId;
        when(valueOperations.get(cacheKey)).thenReturn("true");

        boolean result = featureFlagService.isEnabled("dark-mode", tenantId, userId);

        assertThat(result).isTrue();
        // DB should not be queried
        org.mockito.Mockito.verifyNoInteractions(flagRepository, overrideRepository);
    }

    @Test
    void isEnabled_cachesResultInRedis() {
        stubRedisValueOps();
        String cacheKey = "flags:dark-mode:" + tenantId + ":" + userId;
        when(valueOperations.get(cacheKey)).thenReturn(null);

        FeatureFlag flag = createFlag(flagId1, "dark-mode", true);
        when(flagRepository.findByKey("dark-mode")).thenReturn(Optional.of(flag));
        when(overrideRepository.findByFlagIdAndTenantIdAndUserId(flagId1, tenantId, userId))
                .thenReturn(Optional.empty());
        when(overrideRepository.findByFlagIdAndTenantIdAndUserIdIsNull(flagId1, tenantId))
                .thenReturn(Optional.empty());
        when(overrideRepository.findByFlagIdAndTenantIdIsNullAndUserIdIsNull(flagId1))
                .thenReturn(Optional.empty());

        featureFlagService.isEnabled("dark-mode", tenantId, userId);

        verify(valueOperations).set(eq(cacheKey), eq("true"), eq(Duration.ofMinutes(5)));
    }

    // --- createFlag tests ---

    @Test
    void createFlag_savesAndReturnsFlag() {
        when(flagRepository.findByKey("new-feature")).thenReturn(Optional.empty());

        FeatureFlag saved = createFlag(flagId1, "new-feature", true);
        when(flagRepository.save(any(FeatureFlag.class))).thenReturn(saved);

        FeatureFlag result = featureFlagService.createFlag("new-feature", "A new feature", true);

        assertThat(result.getKey()).isEqualTo("new-feature");
        assertThat(result.isDefaultEnabled()).isTrue();
        verify(flagRepository).save(any(FeatureFlag.class));
    }

    @Test
    void createFlag_throwsWhenDuplicateKey() {
        FeatureFlag existing = createFlag(flagId1, "existing-flag", false);
        when(flagRepository.findByKey("existing-flag")).thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> featureFlagService.createFlag("existing-flag", "desc", false))
                .isInstanceOf(CcException.class)
                .hasFieldOrPropertyWithValue("errorCode", "RESOURCE_CONFLICT");
    }

    // --- listFlags test ---

    @Test
    void listFlags_returnsAllFlags() {
        FeatureFlag f1 = createFlag(flagId1, "flag-a", true);
        FeatureFlag f2 = createFlag(flagId2, "flag-b", false);
        when(flagRepository.findAll()).thenReturn(List.of(f1, f2));

        List<FeatureFlag> result = featureFlagService.listFlags();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(FeatureFlag::getKey).containsExactly("flag-a", "flag-b");
    }

    // --- evaluateAll test ---

    @Test
    void evaluateAll_evaluatesAllFlagsForContext() {
        stubRedisValueOps();
        when(valueOperations.get(anyString())).thenReturn(null);

        FeatureFlag f1 = createFlag(flagId1, "flag-a", true);
        FeatureFlag f2 = createFlag(flagId2, "flag-b", false);
        when(flagRepository.findAll()).thenReturn(List.of(f1, f2));

        // No overrides for either flag
        when(overrideRepository.findByFlagIdAndTenantIdAndUserId(any(), eq(tenantId), eq(userId)))
                .thenReturn(Optional.empty());
        when(overrideRepository.findByFlagIdAndTenantIdAndUserIdIsNull(any(), eq(tenantId)))
                .thenReturn(Optional.empty());
        when(overrideRepository.findByFlagIdAndTenantIdIsNullAndUserIdIsNull(any()))
                .thenReturn(Optional.empty());

        Map<String, Boolean> result = featureFlagService.evaluateAll(tenantId, userId);

        assertThat(result).containsEntry("flag-a", true);
        assertThat(result).containsEntry("flag-b", false);
    }

    // --- setOverride test ---

    @Test
    void setOverride_createsNewOverride() {
        FeatureFlag flag = createFlag(flagId1, "dark-mode", false);
        when(flagRepository.findByKey("dark-mode")).thenReturn(Optional.of(flag));
        when(overrideRepository.findByFlagIdAndTenantIdAndUserId(flagId1, tenantId, userId))
                .thenReturn(Optional.empty());

        UUID overrideId = UUID.randomUUID();
        FlagOverride saved = new FlagOverride();
        saved.setId(overrideId);
        saved.setFlagId(flagId1);
        saved.setTenantId(tenantId);
        saved.setUserId(userId);
        saved.setEnabled(true);
        when(overrideRepository.save(any(FlagOverride.class))).thenReturn(saved);

        FlagOverride result = featureFlagService.setOverride("dark-mode", tenantId, userId, true);

        assertThat(result.isEnabled()).isTrue();
        verify(overrideRepository).save(any(FlagOverride.class));
        verify(redisTemplate).delete(anyString());
    }

    @Test
    void isEnabled_autoCreatesWhenFlagNotFound() {
        stubRedisValueOps();
        when(valueOperations.get(anyString())).thenReturn(null);
        when(flagRepository.findByKey("nonexistent")).thenReturn(Optional.empty());
        when(flagRepository.save(any(FeatureFlag.class))).thenAnswer(inv -> inv.getArgument(0));

        boolean result = featureFlagService.isEnabled("nonexistent", tenantId, userId);

        assertThat(result).isFalse();
        verify(flagRepository).save(any(FeatureFlag.class));
    }
}
