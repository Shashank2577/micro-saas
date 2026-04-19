package com.microsaas.billingai.service;

import com.microsaas.billingai.model.PaymentAttempt;
import com.microsaas.billingai.repository.PaymentAttemptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentAttemptService {
    private final PaymentAttemptRepository repository;

    public List<PaymentAttempt> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    public PaymentAttempt findById(UUID tenantId, UUID id) {
        return repository.findById(id)
                .filter(attempt -> attempt.getTenantId().equals(tenantId))
                .orElseThrow(() -> new RuntimeException("Payment Attempt not found"));
    }

    public PaymentAttempt create(UUID tenantId, PaymentAttempt attempt) {
        attempt.setId(UUID.randomUUID());
        attempt.setTenantId(tenantId);
        attempt.setCreatedAt(OffsetDateTime.now());
        attempt.setUpdatedAt(OffsetDateTime.now());
        return repository.save(attempt);
    }

    public PaymentAttempt update(UUID tenantId, UUID id, PaymentAttempt attemptUpdate) {
        PaymentAttempt existing = findById(tenantId, id);
        existing.setName(attemptUpdate.getName());
        existing.setStatus(attemptUpdate.getStatus());
        existing.setMetadataJson(attemptUpdate.getMetadataJson());
        existing.setUpdatedAt(OffsetDateTime.now());
        return repository.save(existing);
    }

    public boolean validate(UUID tenantId, UUID id) {
        findById(tenantId, id);
        return true;
    }
}
