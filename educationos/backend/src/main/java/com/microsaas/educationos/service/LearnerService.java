package com.microsaas.educationos.service;

import com.microsaas.educationos.domain.entity.LearnerProfile;
import com.microsaas.educationos.domain.repository.LearnerProfileRepository;
import com.microsaas.educationos.dto.LearnerProfileDto;
import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class LearnerService {

    @Autowired
    private LearnerProfileRepository learnerProfileRepository;

    public LearnerProfileDto getLearnerProfile(UUID userId) {
        UUID tenantId = TenantContext.require();
        LearnerProfile profile = learnerProfileRepository.findByUserIdAndTenantId(userId, tenantId)
                .orElse(null);
        return profile != null ? mapToDto(profile) : null;
    }

    public LearnerProfileDto createOrUpdateLearnerProfile(LearnerProfileDto dto) {
        UUID tenantId = TenantContext.require();
        LearnerProfile profile = learnerProfileRepository.findByUserIdAndTenantId(dto.getUserId(), tenantId)
                .orElse(new LearnerProfile());
                
        profile.setTenantId(tenantId);
        profile.setUserId(dto.getUserId());
        profile.setBackgroundInfo(dto.getBackgroundInfo());
        profile.setLearningStyle(dto.getLearningStyle());
        
        profile = learnerProfileRepository.save(profile);
        return mapToDto(profile);
    }

    private LearnerProfileDto mapToDto(LearnerProfile entity) {
        LearnerProfileDto dto = new LearnerProfileDto();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setBackgroundInfo(entity.getBackgroundInfo());
        dto.setLearningStyle(entity.getLearningStyle());
        return dto;
    }
}
