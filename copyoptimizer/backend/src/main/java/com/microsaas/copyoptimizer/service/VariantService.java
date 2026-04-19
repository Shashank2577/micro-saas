package com.microsaas.copyoptimizer.service;

import com.microsaas.copyoptimizer.model.Variant;
import com.microsaas.copyoptimizer.repository.VariantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class VariantService {

    private final VariantRepository repository;

    @Transactional
    public Variant create(Variant entity) {
        Variant saved = repository.save(entity);
        publishEvent("copyoptimizer.variant.generated", saved);
        return saved;
    }

    @Transactional
    public Variant update(UUID id, UUID tenantId, Variant entity) {
        Variant existing = getById(id, tenantId);
        existing.setName(entity.getName());
        existing.setStatus(entity.getStatus());
        existing.setMetadataJson(entity.getMetadataJson());
        Variant saved = repository.save(existing);
        if ("PROMOTED".equalsIgnoreCase(saved.getStatus())) {
            publishEvent("copyoptimizer.variant.promoted", saved);
        }
        return saved;
    }

    @Transactional(readOnly = true)
    public List<Variant> list(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public Variant getById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("Variant not found"));
    }

    @Transactional
    public void delete(UUID id, UUID tenantId) {
        Variant existing = getById(id, tenantId);
        repository.delete(existing);
    }

    public boolean validate(UUID id, UUID tenantId) {
        getById(id, tenantId);
        return true;
    }

    public void simulate(UUID id, UUID tenantId) {
        // Implementation stub
    }

    private void publishEvent(String eventType, Variant variant) {
        log.info("Publishing domain event {}: {}", eventType, variant.getId());
        // In a real application, this would use a messaging broker (Kafka, RabbitMQ, pgmq)
        // or a Spring ApplicationEventPublisher to integrate with Nexus Hub
    }
}
