package com.microsaas.notificationhub.service;

import com.microsaas.notificationhub.api.dto.PreferenceDto;
import com.microsaas.notificationhub.domain.entity.UserPreference;
import com.microsaas.notificationhub.domain.repository.UserPreferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PreferenceService {

    private final UserPreferenceRepository preferenceRepository;
    private final org.springframework.data.redis.core.StringRedisTemplate redisTemplate;

    @Transactional(readOnly = true)
    public List<PreferenceDto> getUserPreferences(String tenantId, String userId) {
        return preferenceRepository.findByTenantIdAndUserId(tenantId, userId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public PreferenceDto updatePreference(String tenantId, String userId, String channel, Boolean optedIn) {
        UserPreference preference = preferenceRepository.findByTenantIdAndUserIdAndChannel(tenantId, userId, channel)
                .orElseGet(() -> UserPreference.builder()
                        .id(UUID.randomUUID())
                        .tenantId(tenantId)
                        .userId(userId)
                        .channel(channel)
                        .build());

        preference.setOptedIn(optedIn);
        preferenceRepository.save(preference);
        return mapToDto(preference);
    }

    @Transactional(readOnly = true)
    public boolean isUserOptedIn(String tenantId, String userId, String channel) {
        return preferenceRepository.findByTenantIdAndUserIdAndChannel(tenantId, userId, channel)
                .map(UserPreference::getOptedIn)
                .orElse(true); // Default to true if no explicit preference is set
    }

    public boolean checkRateLimit(String tenantId, String userId, String channel) {
        String key = "rate_limit:" + tenantId + ":" + userId + ":" + channel;
        Long count = redisTemplate.opsForValue().increment(key);
        if (count == 1) {
            // Expire after 1 day
            redisTemplate.expire(key, java.time.Duration.ofDays(1));
        }
        // 100 notifications/user/day default
        return count != null && count <= 100;
    }

    private PreferenceDto mapToDto(UserPreference preference) {
        return PreferenceDto.builder()
                .id(preference.getId())
                .userId(preference.getUserId())
                .channel(preference.getChannel())
                .optedIn(preference.getOptedIn())
                .build();
    }
}
