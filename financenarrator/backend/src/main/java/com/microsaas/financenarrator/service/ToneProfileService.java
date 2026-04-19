package com.microsaas.financenarrator.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.financenarrator.model.ToneProfile;
import com.microsaas.financenarrator.repository.ToneProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import com.microsaas.financenarrator.service.EventPublisherService;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ToneProfileService {
    private final EventPublisherService eventPublisher;
    private final ToneProfileRepository repository;

    @Transactional
    public ToneProfile create(ToneProfile dto) {
        dto.setTenantId(TenantContext.require());
        var result = repository.save(dto);
        eventPublisher.publishEvent("financenarrator.narrative.generated", dto.getTenantId(), "financenarrator", Map.of("id", dto.getId()));
        return result;
    }

    @Transactional
    public ToneProfile update(UUID id, ToneProfile dto) {
        ToneProfile existing = getById(id);
        existing.setName(dto.getName());
        existing.setStatus(dto.getStatus());
        existing.setMetadataJson(dto.getMetadataJson());
        return repository.save(existing);
    }

    public List<ToneProfile> list() {
        return repository.findByTenantId(TenantContext.require());
    }

    public ToneProfile getById(UUID id) {
        return repository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("ToneProfile not found"));
    }

    @Transactional
    public void delete(UUID id) {
        ToneProfile existing = getById(id);
        repository.delete(existing);
    }

    public void validate(UUID id) {
        ToneProfile existing = getById(id);
        if ("DRAFT".equals(existing.getStatus())) {
            existing.setStatus("VALIDATED");
            repository.save(existing);
        }
    }

    public void simulate(UUID id) {
        ToneProfile existing = getById(id);
        if ("VALIDATED".equals(existing.getStatus())) {
            existing.setStatus("SIMULATED");
            repository.save(existing);
        }
    }
}
