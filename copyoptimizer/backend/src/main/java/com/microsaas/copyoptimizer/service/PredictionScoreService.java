package com.microsaas.copyoptimizer.service;

import com.microsaas.copyoptimizer.model.PredictionScore;
import com.microsaas.copyoptimizer.repository.PredictionScoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PredictionScoreService {

    private final PredictionScoreRepository repository;

    @Transactional
    public PredictionScore create(PredictionScore entity) {
        PredictionScore saved = repository.save(entity);
        publishEvent("copyoptimizer.score.computed", saved);
        return saved;
    }

    @Transactional
    public PredictionScore update(UUID id, UUID tenantId, PredictionScore entity) {
        PredictionScore existing = getById(id, tenantId);
        existing.setName(entity.getName());
        existing.setStatus(entity.getStatus());
        existing.setMetadataJson(entity.getMetadataJson());
        PredictionScore saved = repository.save(existing);
        publishEvent("copyoptimizer.score.computed", saved);
        return saved;
    }

    @Transactional(readOnly = true)
    public List<PredictionScore> list(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public PredictionScore getById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("PredictionScore not found"));
    }

    @Transactional
    public void delete(UUID id, UUID tenantId) {
        PredictionScore existing = getById(id, tenantId);
        repository.delete(existing);
    }

    public boolean validate(UUID id, UUID tenantId) {
        getById(id, tenantId);
        return true;
    }

    public void simulate(UUID id, UUID tenantId) {
        // Implementation stub
    }

    private void publishEvent(String eventType, PredictionScore score) {
        log.info("Publishing domain event {}: {}", eventType, score.getId());
        // Messaging broker abstraction placeholder
    }
}
