package com.microsaas.financenarrator.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.financenarrator.model.NarrativeSection;
import com.microsaas.financenarrator.repository.NarrativeSectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import com.microsaas.financenarrator.service.EventPublisherService;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NarrativeSectionService {
    private final EventPublisherService eventPublisher;
    private final NarrativeSectionRepository repository;

    @Transactional
    public NarrativeSection create(NarrativeSection dto) {
        dto.setTenantId(TenantContext.require());
        var result = repository.save(dto);
        eventPublisher.publishEvent("financenarrator.narrative.generated", dto.getTenantId(), "financenarrator", Map.of("id", dto.getId()));
        return result;
    }

    @Transactional
    public NarrativeSection update(UUID id, NarrativeSection dto) {
        NarrativeSection existing = getById(id);
        existing.setName(dto.getName());
        existing.setStatus(dto.getStatus());
        existing.setMetadataJson(dto.getMetadataJson());
        return repository.save(existing);
    }

    public List<NarrativeSection> list() {
        return repository.findByTenantId(TenantContext.require());
    }

    public NarrativeSection getById(UUID id) {
        return repository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("NarrativeSection not found"));
    }

    @Transactional
    public void delete(UUID id) {
        NarrativeSection existing = getById(id);
        repository.delete(existing);
    }

    public void validate(UUID id) {
        NarrativeSection existing = getById(id);
        if ("DRAFT".equals(existing.getStatus())) {
            existing.setStatus("VALIDATED");
            repository.save(existing);
        }
    }

    public void simulate(UUID id) {
        NarrativeSection existing = getById(id);
        if ("VALIDATED".equals(existing.getStatus())) {
            existing.setStatus("SIMULATED");
            repository.save(existing);
        }
    }
}
