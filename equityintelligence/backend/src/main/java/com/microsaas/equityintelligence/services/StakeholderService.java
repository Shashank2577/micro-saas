package com.microsaas.equityintelligence.services;

import com.microsaas.equityintelligence.model.Stakeholder;
import com.microsaas.equityintelligence.repositories.StakeholderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StakeholderService {

    private final StakeholderRepository stakeholderRepository;

    public Stakeholder createStakeholder(Stakeholder stakeholder, UUID tenantId) {
        stakeholder.setTenantId(tenantId);
        return stakeholderRepository.save(stakeholder);
    }

    public List<Stakeholder> getStakeholders(UUID tenantId) {
        return stakeholderRepository.findAllByTenantId(tenantId);
    }
}
