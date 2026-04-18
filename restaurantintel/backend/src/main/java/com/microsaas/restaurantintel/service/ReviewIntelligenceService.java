package com.microsaas.restaurantintel.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.restaurantintel.domain.CustomerReview;
import com.microsaas.restaurantintel.repository.CustomerReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ReviewIntelligenceService {

    private final CustomerReviewRepository reviewRepository;
    private final LiteLLMClient liteLLMClient;

    public ReviewIntelligenceService(CustomerReviewRepository reviewRepository, LiteLLMClient liteLLMClient) {
        this.reviewRepository = reviewRepository;
        this.liteLLMClient = liteLLMClient;
    }

    public List<CustomerReview> getReviews() {
        return reviewRepository.findByTenantId(TenantContext.require());
    }

    public CustomerReview createReview(CustomerReview review) {
        review.setTenantId(TenantContext.require());
        return reviewRepository.save(review);
    }

    public CustomerReview analyzeReview(UUID reviewId) {
        CustomerReview review = reviewRepository.findById(reviewId).orElseThrow();
        String prompt = "Analyze the sentiment (POSITIVE, NEUTRAL, NEGATIVE) and extract an operational insight from this review. Output in format: 'SENTIMENT | INSIGHT'. Review: " + review.getContent();
        
        String response = liteLLMClient.completeChat(prompt);
        if (response != null && response.contains("|")) {
            String[] parts = response.split("\\|", 2);
            review.setSentiment(parts[0].trim());
            review.setOperationalInsight(parts[1].trim());
        } else {
            review.setSentiment("NEUTRAL");
            review.setOperationalInsight(response);
        }
        
        return reviewRepository.save(review);
    }
}
