package com.microsaas.legalresearch.service;

import com.microsaas.legalresearch.domain.ReviewComment;
import com.microsaas.legalresearch.repository.ReviewCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewCommentService {
    private final ReviewCommentRepository repository;

    @Transactional(readOnly = true)
    public List<ReviewComment> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public ReviewComment findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("ReviewComment not found"));
    }

    @Transactional
    public ReviewComment create(ReviewComment entity) {
        return repository.save(entity);
    }

    @Transactional
    public ReviewComment update(UUID id, ReviewComment entity, UUID tenantId) {
        ReviewComment existing = findById(id, tenantId);
        existing.setName(entity.getName());
        existing.setStatus(entity.getStatus());
        existing.setMetadataJson(entity.getMetadataJson());
        return repository.save(existing);
    }

    @Transactional
    public void delete(UUID id, UUID tenantId) {
        ReviewComment existing = findById(id, tenantId);
        repository.delete(existing);
    }

    public boolean validate(UUID id, UUID tenantId) {
        findById(id, tenantId);
        return true;
    }
}
