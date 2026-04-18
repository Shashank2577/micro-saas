package com.marketplacehub.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.marketplacehub.dto.InstallRequest;
import com.marketplacehub.model.*;
import com.marketplacehub.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class MarketplaceService {

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private AppInstallationRepository installationRepository;

    @Autowired
    private AppReviewRepository reviewRepository;

    @Autowired
    private AppRevenueRepository revenueRepository;

    @Autowired
    private PermissionGrantRepository permissionGrantRepository;

    public List<App> getApps(String category, String searchText) {
        UUID tenantId = TenantContext.require();
        if (searchText != null && !searchText.isEmpty()) {
            return appRepository.searchApps(tenantId, searchText);
        } else if (category != null && !category.isEmpty()) {
            return appRepository.findByTenantIdAndCategory(tenantId, category);
        } else {
            return appRepository.findByTenantId(tenantId);
        }
    }

    public App getAppById(UUID id) {
        UUID tenantId = TenantContext.require();
        return appRepository.findById(id)
                .filter(a -> a.getTenantId().equals(tenantId))
                .orElseThrow(() -> new RuntimeException("App not found"));
    }

    @Transactional
    public App registerApp(App app) {
        app.setTenantId(TenantContext.require());
        return appRepository.save(app);
    }

    @Transactional
    public AppInstallation installApp(UUID appId, InstallRequest request) {
        UUID tenantId = TenantContext.require();
        App app = getAppById(appId);

        AppInstallation installation = new AppInstallation();
        installation.setTenantId(tenantId);
        installation.setAppId(appId);
        installation.setWorkspaceId(request.getWorkspaceId());
        installation.setInstalledAt(LocalDateTime.now());
        installation.setTrialEndsAt(LocalDateTime.now().plusDays(14));
        installation = installationRepository.save(installation);

        for (String perm : request.getPermissions()) {
            PermissionGrant grant = new PermissionGrant();
            grant.setTenantId(tenantId);
            grant.setInstallationId(installation.getId());
            grant.setPermissionName(perm);
            permissionGrantRepository.save(grant);
        }

        app.setTotalInstallations(app.getTotalInstallations() + 1);
        appRepository.save(app);

        return installation;
    }

    public List<AppInstallation> getWorkspaceInstallations(String workspaceId) {
        return installationRepository.findByTenantIdAndWorkspaceId(TenantContext.require(), workspaceId);
    }

    @Transactional
    public AppReview submitReview(UUID appId, AppReview review) {
        review.setTenantId(TenantContext.require());
        review.setAppId(appId);
        return reviewRepository.save(review);
    }

    public List<AppReview> getApprovedReviews(UUID appId) {
        return reviewRepository.findByTenantIdAndAppIdAndStatus(TenantContext.require(), appId, "APPROVED");
    }

    @Transactional
    public AppReview updateReviewStatus(UUID reviewId, String status) {
        UUID tenantId = TenantContext.require();
        AppReview review = reviewRepository.findById(reviewId)
                .filter(r -> r.getTenantId().equals(tenantId))
                .orElseThrow(() -> new RuntimeException("Review not found"));
        review.setStatus(status);
        return reviewRepository.save(review);
    }

    public List<App> getTrendingApps() {
        return appRepository.findTop10ByTenantIdOrderByTotalInstallationsDesc(TenantContext.require());
    }

    public List<AppRevenue> getAppRevenues(UUID appId) {
        return revenueRepository.findByTenantIdAndAppId(TenantContext.require(), appId);
    }
}
