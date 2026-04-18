package com.microsaas.identitycore.service;

import com.microsaas.identitycore.model.AccessReview;
import com.microsaas.identitycore.repository.AccessReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AccessReviewServiceTest {

    @Mock
    private AccessReviewRepository accessReviewRepository;

    @Mock
    private AIAnalysisService aiAnalysisService;

    @InjectMocks
    private AccessReviewService accessReviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateReview() {
        UUID tenantId = UUID.randomUUID();
        AccessReview review = new AccessReview();

        when(accessReviewRepository.save(any(AccessReview.class))).thenReturn(review);

        AccessReview created = accessReviewService.createReview(tenantId, review);

        assertNotNull(created);
        assertEquals("PENDING", review.getStatus());
        assertNotNull(review.getCreatedAt());
        verify(accessReviewRepository, times(1)).save(review);
    }

    @Test
    void testGenerateRecommendation() {
        UUID id = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();
        AccessReview review = new AccessReview();
        review.setId(id);
        review.setTenantId(tenantId);

        when(accessReviewRepository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(review));
        when(aiAnalysisService.generateReviewRecommendation(anyString(), anyString(), anyString())).thenReturn("{\"action\": \"REVOKE\"}");
        when(accessReviewRepository.save(any(AccessReview.class))).thenReturn(review);

        AccessReview updated = accessReviewService.generateRecommendation(id, tenantId);

        assertNotNull(updated);
        assertEquals("{\"action\": \"REVOKE\"}", updated.getAiRecommendation());
        verify(accessReviewRepository, times(1)).save(review);
    }
}
