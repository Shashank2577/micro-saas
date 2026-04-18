package com.microsaas.retirementplus.service;

import com.microsaas.retirementplus.domain.UserProfile;
import com.microsaas.retirementplus.dto.ProfileDto;
import com.microsaas.retirementplus.repository.UserProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import java.util.Map;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.Optional;

@Service
public class ProfileService {

    private final UserProfileRepository userProfileRepository;
    private final ApplicationEventPublisher eventPublisher;

    public ProfileService(UserProfileRepository userProfileRepository, ApplicationEventPublisher eventPublisher) {
        this.userProfileRepository = userProfileRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public UserProfile createOrUpdateProfile(ProfileDto dto, UUID tenantId) {
        Optional<UserProfile> existingProfileOpt = userProfileRepository.findByUserIdAndTenantId(dto.getUserId(), tenantId);
        
        UserProfile profile = existingProfileOpt.orElse(new UserProfile());
        if (existingProfileOpt.isEmpty()) {
            profile.setId(UUID.randomUUID());
            profile.setTenantId(tenantId);
            profile.setCreatedAt(ZonedDateTime.now());
        }
        
        profile.setUserId(dto.getUserId());
        profile.setCurrentAge(dto.getCurrentAge());
        profile.setRetirementAge(dto.getRetirementAge());
        profile.setCurrentSavings(dto.getCurrentSavings());
        profile.setDesiredIncome(dto.getDesiredIncome());
        profile.setGender(dto.getGender());
        profile.setHealthStatus(dto.getHealthStatus());
        profile.setFamilyHistory(dto.getFamilyHistory());
        profile.setInheritanceGoal(dto.getInheritanceGoal() != null ? dto.getInheritanceGoal() : java.math.BigDecimal.ZERO);
        profile.setWantsAnnuity(dto.getWantsAnnuity() != null ? dto.getWantsAnnuity() : false);
        profile.setUpdatedAt(ZonedDateTime.now());

        UserProfile saved = userProfileRepository.save(profile);
        
        // Emit event
        eventPublisher.publishEvent(Map.of(
            "eventType", "retirementplus.profile_created",
            "tenantId", tenantId,
            "userId", dto.getUserId(),
            "profileId", saved.getId()
        ));
        
        return saved;
    }

    public Optional<UserProfile> getProfileByUserId(UUID userId, UUID tenantId) {
        return userProfileRepository.findByUserIdAndTenantId(userId, tenantId);
    }
}
