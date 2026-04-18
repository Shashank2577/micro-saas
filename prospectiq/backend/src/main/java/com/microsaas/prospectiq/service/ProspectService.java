package com.microsaas.prospectiq.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.prospectiq.dto.ProspectRequest;
import com.microsaas.prospectiq.model.Prospect;
import com.microsaas.prospectiq.repository.ProspectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProspectService {
    private final ProspectRepository prospectRepository;

    @Transactional(readOnly = true)
    public List<Prospect> getAllProspects(String industry, String region) {
        UUID tenantId = TenantContext.require();
        if (industry != null) {
            return prospectRepository.findByTenantIdAndIndustry(tenantId, industry);
        } else if (region != null) {
            return prospectRepository.findByTenantIdAndRegion(tenantId, region);
        }
        return prospectRepository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public Prospect getProspect(UUID id) {
        UUID tenantId = TenantContext.require();
        return prospectRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("Prospect not found"));
    }

    @Transactional
    public Prospect createProspect(ProspectRequest request) {
        UUID tenantId = TenantContext.require();
        Prospect prospect = Prospect.builder()
                .tenantId(tenantId)
                .name(request.getName())
                .domain(request.getDomain())
                .industry(request.getIndustry())
                .region(request.getRegion())
                .crmId(request.getCrmId())
                .build();
        return prospectRepository.save(prospect);
    }
}
