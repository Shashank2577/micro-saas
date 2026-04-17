package com.microsaas.brandvoice.service;

import com.microsaas.brandvoice.dto.BrandProfileRequest;
import com.microsaas.brandvoice.entity.BrandProfile;
import com.microsaas.brandvoice.repository.BrandProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BrandProfileService {

    private final BrandProfileRepository repository;

    public BrandProfile createProfile(UUID tenantId, BrandProfileRequest request) {
        BrandProfile profile = new BrandProfile();
        profile.setTenantId(tenantId);
        profile.setName(request.getName());
        profile.setTone(request.getTone());
        profile.setVocabularyAllowed(request.getVocabularyAllowed());
        profile.setVocabularyForbidden(request.getVocabularyForbidden());
        profile.setCoreValues(request.getCoreValues());
        return repository.save(profile);
    }

    public List<BrandProfile> getProfiles(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    public BrandProfile getProfile(UUID tenantId, UUID id) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    public BrandProfile updateProfile(UUID tenantId, UUID id, BrandProfileRequest request) {
        BrandProfile profile = getProfile(tenantId, id);
        profile.setName(request.getName());
        profile.setTone(request.getTone());
        profile.setVocabularyAllowed(request.getVocabularyAllowed());
        profile.setVocabularyForbidden(request.getVocabularyForbidden());
        profile.setCoreValues(request.getCoreValues());
        return repository.save(profile);
    }

    public void deleteProfile(UUID tenantId, UUID id) {
        BrandProfile profile = getProfile(tenantId, id);
        repository.delete(profile);
    }
}
