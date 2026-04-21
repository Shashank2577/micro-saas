package com.microsaas.financenarrator.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.financenarrator.model.ApprovalReview;
import com.microsaas.financenarrator.repository.ApprovalReviewRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApprovalReviewServiceTest {

    @Mock
    private ApprovalReviewRepository repository;

    @Mock
    private EventPublisherService eventPublisher;

    @InjectMocks
    private ApprovalReviewService service;

    private MockedStatic<TenantContext> tenantContextMockedStatic;
    private UUID tenantId;
    private UUID reviewId;
    private ApprovalReview review;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        reviewId = UUID.randomUUID();
        tenantContextMockedStatic = Mockito.mockStatic(TenantContext.class);
        tenantContextMockedStatic.when(TenantContext::require).thenReturn(tenantId);

        review = new ApprovalReview();
        review.setId(reviewId);
        review.setTenantId(tenantId);
        review.setName("CFO Review");
        review.setStatus("DRAFT");
    }

    @AfterEach
    void tearDown() {
        tenantContextMockedStatic.close();
    }

    @Test
    void testSubmitForReview_success() {
        when(repository.save(any(ApprovalReview.class))).thenReturn(review);

        ApprovalReview result = service.create(review);

        assertNotNull(result);
        assertEquals(tenantId, result.getTenantId());
        verify(repository, times(1)).save(review);
        verify(eventPublisher, times(1)).publishEvent(
                eq("financenarrator.narrative.generated"),
                eq(tenantId),
                eq("financenarrator"),
                any(Map.class)
        );
    }

    @Test
    void testApproveReview() {
        when(repository.findByIdAndTenantId(reviewId, tenantId)).thenReturn(Optional.of(review));
        when(repository.save(any(ApprovalReview.class))).thenReturn(review);

        service.validate(reviewId);

        verify(repository, times(1)).findByIdAndTenantId(reviewId, tenantId);
        verify(repository, times(1)).save(review);
        assertEquals("VALIDATED", review.getStatus());
    }

    @Test
    void testRejectReview() {
        review.setStatus("VALIDATED");
        when(repository.findByIdAndTenantId(reviewId, tenantId)).thenReturn(Optional.of(review));
        when(repository.save(any(ApprovalReview.class))).thenReturn(review);

        service.simulate(reviewId);

        verify(repository, times(1)).findByIdAndTenantId(reviewId, tenantId);
        verify(repository, times(1)).save(review);
        assertEquals("SIMULATED", review.getStatus());
    }
}
