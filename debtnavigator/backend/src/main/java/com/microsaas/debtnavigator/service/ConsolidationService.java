package com.microsaas.debtnavigator.service;

import com.microsaas.debtnavigator.entity.ConsolidationOffer;
import com.microsaas.debtnavigator.repository.ConsolidationOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConsolidationService {
    private final ConsolidationOfferRepository repository;

    public ConsolidationOffer create(ConsolidationOffer offer) {
        return repository.save(offer);
    }

    public List<ConsolidationOffer> list(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    public Optional<ConsolidationOffer> getById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId);
    }

    public ConsolidationOffer update(ConsolidationOffer offer) {
        return repository.save(offer);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public boolean validate(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId).isPresent();
    }

    public Object simulate(UUID id, UUID tenantId) {
        return "Simulation result for consolidation offer " + id;
    }
}
