package com.microsaas.identitycore.service;

import com.microsaas.identitycore.model.AccessReview;
import com.microsaas.identitycore.repository.AccessReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccessReviewService {

    private final AccessReviewRepository accessReviewRepository;
    private final AIAnalysisService aiAnalysisService;

    public AccessReviewService(AccessReviewRepository accessReviewRepository, AIAnalysisService aiAnalysisService) {
        this.accessReviewRepository = accessReviewRepository;
        this.aiAnalysisService = aiAnalysisService;
    }

    public List<AccessReview> getReviewsByTenant(UUID tenantId) {
        return accessReviewRepository.findByTenantId(tenantId);
    }

    public Optional<AccessReview> getReviewById(UUID id, UUID tenantId) {
        return accessReviewRepository.findByIdAndTenantId(id, tenantId);
    }

    @Transactional
    public AccessReview createReview(UUID tenantId, AccessReview review) {
        review.setId(UUID.randomUUID());
        review.setTenantId(tenantId);
        review.setCreatedAt(OffsetDateTime.now());
        review.setUpdatedAt(OffsetDateTime.now());
        if (review.getStatus() == null) {
            review.setStatus("PENDING");
        }
        return accessReviewRepository.save(review);
    }

    @Transactional
    public AccessReview updateReview(UUID id, UUID tenantId, AccessReview updatedReview) {
        return accessReviewRepository.findByIdAndTenantId(id, tenantId).map(review -> {
            review.setStatus(updatedReview.getStatus() != null ? updatedReview.getStatus() : review.getStatus());
            review.setAiRecommendation(updatedReview.getAiRecommendation() != null ? updatedReview.getAiRecommendation() : review.getAiRecommendation());
            review.setUpdatedAt(OffsetDateTime.now());
            return accessReviewRepository.save(review);
        }).orElseThrow(() -> new RuntimeException("Access Review not found"));
    }

    @Transactional
    public AccessReview generateRecommendation(UUID id, UUID tenantId) {
        return accessReviewRepository.findByIdAndTenantId(id, tenantId).map(review -> {
            // Mocking the inputs for AI service for now. In a real scenario, fetch real user/privilege data.
            String recommendation = aiAnalysisService.generateReviewRecommendation("{}", "{}", "{}");
            review.setAiRecommendation(recommendation);
            review.setUpdatedAt(OffsetDateTime.now());
            return accessReviewRepository.save(review);
        }).orElseThrow(() -> new RuntimeException("Access Review not found"));
    }
}
