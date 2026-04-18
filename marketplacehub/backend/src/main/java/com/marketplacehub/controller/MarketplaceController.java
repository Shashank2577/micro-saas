package com.marketplacehub.controller;

import com.marketplacehub.dto.InstallRequest;
import com.marketplacehub.model.*;
import com.marketplacehub.service.MarketplaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "MarketplaceHub API", description = "Endpoints for Marketplace app discovery, installation and reviews")
public class MarketplaceController {

    @Autowired
    private MarketplaceService marketplaceService;

    @GetMapping("/apps")
    @Operation(summary = "List apps")
    public List<App> getApps(@RequestParam(required = false) String category,
                             @RequestParam(required = false) String searchText) {
        return marketplaceService.getApps(category, searchText);
    }

    @GetMapping("/apps/{id}")
    @Operation(summary = "Get app by ID")
    public App getApp(@PathVariable UUID id) {
        return marketplaceService.getAppById(id);
    }

    @PostMapping("/apps")
    @Operation(summary = "Register new app")
    public App registerApp(@RequestBody App app) {
        return marketplaceService.registerApp(app);
    }

    @PostMapping("/apps/{id}/install")
    @Operation(summary = "Install an app")
    public AppInstallation installApp(@PathVariable UUID id, @RequestBody InstallRequest request) {
        return marketplaceService.installApp(id, request);
    }

    @GetMapping("/installations/workspace/{workspaceId}")
    @Operation(summary = "Get installations for a workspace")
    public List<AppInstallation> getInstallations(@PathVariable String workspaceId) {
        return marketplaceService.getWorkspaceInstallations(workspaceId);
    }

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

    @GetMapping("/apps/trending")
    @Operation(summary = "Get trending apps")
    public List<App> getTrendingApps() {
        return marketplaceService.getTrendingApps();
    }

    @GetMapping("/apps/{id}/revenue")
    @Operation(summary = "Get app revenues")
    public List<AppRevenue> getAppRevenues(@PathVariable UUID id) {
        return marketplaceService.getAppRevenues(id);
    }
}
