package com.marketplacehub.controller;

import com.marketplacehub.model.AppReview;
import com.marketplacehub.service.MarketplaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "App Review API", description = "Endpoints for app reviews and moderation")
public class AppReviewController {

    @Autowired
    private MarketplaceService marketplaceService;

    @PostMapping("/apps/{id}/reviews")
    @Operation(summary = "Submit an app review")
    public AppReview submitReview(@PathVariable UUID id, @RequestBody AppReview review) {
        return marketplaceService.submitReview(id, review);
    }

    @GetMapping("/apps/{id}/reviews")
    @Operation(summary = "Get approved reviews for an app")
    public List<AppReview> getReviews(@PathVariable UUID id) {
        return marketplaceService.getApprovedReviews(id);
    }

    @PutMapping("/reviews/{reviewId}/status")
    @Operation(summary = "Update review status")
    public AppReview updateReviewStatus(@PathVariable UUID reviewId, @RequestParam String status) {
        return marketplaceService.updateReviewStatus(reviewId, status);
    }
}
