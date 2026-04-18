package com.microsaas.restaurantintel.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.restaurantintel.domain.CustomerReview;
import com.microsaas.restaurantintel.repository.CustomerReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewIntelligenceServiceTest {

    @Mock
    private CustomerReviewRepository reviewRepository;

    @Mock
    private LiteLLMClient liteLLMClient;

    @InjectMocks
    private ReviewIntelligenceService reviewIntelligenceService;

    private UUID tenantId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);
    }

    @Test
    void testGetReviews() {
        CustomerReview review = new CustomerReview();
        review.setSource("Yelp");
        when(reviewRepository.findByTenantId(tenantId)).thenReturn(List.of(review));

        List<CustomerReview> reviews = reviewIntelligenceService.getReviews();
        assertEquals(1, reviews.size());
        assertEquals("Yelp", reviews.get(0).getSource());
    }

    @Test
    void testAnalyzeReview() {
        UUID reviewId = UUID.randomUUID();
        CustomerReview review = new CustomerReview();
        review.setContent("Food was cold but staff was nice.");

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(liteLLMClient.completeChat(anyString())).thenReturn("MIXED | Cold food issue");
        when(reviewRepository.save(any(CustomerReview.class))).thenAnswer(i -> i.getArguments()[0]);

        CustomerReview updated = reviewIntelligenceService.analyzeReview(reviewId);
        assertEquals("MIXED", updated.getSentiment());
        assertEquals("Cold food issue", updated.getOperationalInsight());
    }
}
