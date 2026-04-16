package com.microsaas.performancenarrative.controller;

import com.microsaas.performancenarrative.entity.EmployeeReview;
import com.microsaas.performancenarrative.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<EmployeeReview> createReview(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestBody EmployeeReview review) {
        review.setTenantId(tenantId);
        return ResponseEntity.ok(reviewService.createReview(review));
    }

    @PostMapping("/{reviewId}/generate-draft")
    public ResponseEntity<EmployeeReview> generateDraftNarrative(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID reviewId) {
        // In a real app we might verify the tenantId matches the review's tenantId.
        return ResponseEntity.ok(reviewService.generateDraftNarrative(reviewId));
    }

    @PostMapping("/{reviewId}/finalize")
    public ResponseEntity<EmployeeReview> finalizeReview(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID reviewId) {
        // In a real app we might verify the tenantId matches the review's tenantId.
        return ResponseEntity.ok(reviewService.finalizeReview(reviewId));
    }
}
