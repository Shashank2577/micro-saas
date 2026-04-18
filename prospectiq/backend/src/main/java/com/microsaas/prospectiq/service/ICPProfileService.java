package com.microsaas.prospectiq.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.prospectiq.dto.ICPProfileRequest;
import com.microsaas.prospectiq.model.ICPProfile;
import com.microsaas.prospectiq.repository.ICPProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ICPProfileService {
    private final ICPProfileRepository icpProfileRepository;

    @Transactional(readOnly = true)
    public List<ICPProfile> getAllProfiles() {
        UUID tenantId = TenantContext.require();
        return icpProfileRepository.findByTenantId(tenantId);
    }

    @Transactional
    public ICPProfile createProfile(ICPProfileRequest request) {
        UUID tenantId = TenantContext.require();
        ICPProfile profile = ICPProfile.builder()
                .tenantId(tenantId)
                .name(request.getName())
                .criteria(request.getCriteria())
                .build();
        return icpProfileRepository.save(profile);
    }
}
