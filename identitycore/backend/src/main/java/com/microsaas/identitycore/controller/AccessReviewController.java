package com.microsaas.identitycore.controller;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.identitycore.model.AccessReview;
import com.microsaas.identitycore.service.AccessReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
public class AccessReviewController {

    private final AccessReviewService accessReviewService;

    public AccessReviewController(AccessReviewService accessReviewService) {
        this.accessReviewService = accessReviewService;
    }

    private UUID getTenantId() {
        return UUID.fromString(TenantContext.require().toString());
    }

    @GetMapping
    public ResponseEntity<List<AccessReview>> listReviews() {
        return ResponseEntity.ok(accessReviewService.getReviewsByTenant(getTenantId()));
    }

    @PostMapping
    public ResponseEntity<AccessReview> createReview(@RequestBody AccessReview review) {
        return ResponseEntity.ok(accessReviewService.createReview(getTenantId(), review));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccessReview> getReview(@PathVariable UUID id) {
        return accessReviewService.getReviewById(id, getTenantId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccessReview> updateReview(@PathVariable UUID id, @RequestBody AccessReview review) {
        try {
            return ResponseEntity.ok(accessReviewService.updateReview(id, getTenantId(), review));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/generate-recommendation")
    public ResponseEntity<AccessReview> generateRecommendation(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(accessReviewService.generateRecommendation(id, getTenantId()));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
