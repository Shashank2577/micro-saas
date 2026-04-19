package com.microsaas.financenarrator.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.financenarrator.model.ApprovalReview;
import com.microsaas.financenarrator.repository.ApprovalReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import com.microsaas.financenarrator.service.EventPublisherService;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ApprovalReviewService {
    private final EventPublisherService eventPublisher;
    private final ApprovalReviewRepository repository;

    @Transactional
    public ApprovalReview create(ApprovalReview dto) {
        dto.setTenantId(TenantContext.require());
        var result = repository.save(dto);
        eventPublisher.publishEvent("financenarrator.narrative.generated", dto.getTenantId(), "financenarrator", Map.of("id", dto.getId()));
        return result;
    }

    @Transactional
    public ApprovalReview update(UUID id, ApprovalReview dto) {
        ApprovalReview existing = getById(id);
        existing.setName(dto.getName());
        existing.setStatus(dto.getStatus());
        existing.setMetadataJson(dto.getMetadataJson());
        return repository.save(existing);
    }

    public List<ApprovalReview> list() {
        return repository.findByTenantId(TenantContext.require());
    }

    public ApprovalReview getById(UUID id) {
        return repository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("ApprovalReview not found"));
    }

    @Transactional
    public void delete(UUID id) {
        ApprovalReview existing = getById(id);
        repository.delete(existing);
    }

    public void validate(UUID id) {
        ApprovalReview existing = getById(id);
        if ("DRAFT".equals(existing.getStatus())) {
            existing.setStatus("VALIDATED");
            repository.save(existing);
        }
    }

    public void simulate(UUID id) {
        ApprovalReview existing = getById(id);
        if ("VALIDATED".equals(existing.getStatus())) {
            existing.setStatus("SIMULATED");
            repository.save(existing);
        }
    }
}
