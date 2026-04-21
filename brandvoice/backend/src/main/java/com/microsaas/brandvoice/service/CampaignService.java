package com.microsaas.brandvoice.service;

import com.microsaas.brandvoice.entity.Campaign;
import com.microsaas.brandvoice.repository.CampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CampaignService {
    private final CampaignRepository repository;

    public List<Campaign> findAllByTenant(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    public Campaign save(Campaign entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }
}
