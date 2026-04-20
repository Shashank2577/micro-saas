package com.microsaas.revopsai.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.revopsai.model.GtmGap;
import com.microsaas.revopsai.repository.GtmGapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GtmGapService {
    private final GtmGapRepository repository;

    public List<GtmGap> getAll() {
        UUID tenantId = TenantContext.require();
        return repository.findByTenantId(tenantId);
    }

    public GtmGap create(GtmGap entity) {
        entity.setTenantId(TenantContext.require());
        return repository.save(entity);
    }
}
