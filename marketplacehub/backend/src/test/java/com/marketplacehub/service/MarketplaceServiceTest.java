package com.marketplacehub.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.marketplacehub.dto.InstallRequest;
import com.marketplacehub.model.App;
import com.marketplacehub.model.AppInstallation;
import com.marketplacehub.model.AppReview;
import com.marketplacehub.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MarketplaceServiceTest {

    @Mock
    private AppRepository appRepository;
    @Mock
    private AppInstallationRepository installationRepository;
    @Mock
    private AppReviewRepository reviewRepository;
    @Mock
    private PermissionGrantRepository permissionGrantRepository;

    @InjectMocks
    private MarketplaceService marketplaceService;

    private UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        TenantContext.set(tenantId);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testRegisterApp() {
        App app = new App();
        app.setName("WealthEdge");
        when(appRepository.save(any(App.class))).thenReturn(app);

        App result = marketplaceService.registerApp(app);
        assertNotNull(result);
        assertEquals("WealthEdge", result.getName());
        verify(appRepository, times(1)).save(app);
    }

    @Test
    void testGetAppsBySearch() {
        App app = new App();
        app.setName("WealthEdge");
        when(appRepository.searchApps(tenantId, "wealth")).thenReturn(List.of(app));

        List<App> result = marketplaceService.getApps(null, "wealth");
        assertEquals(1, result.size());
        assertEquals("WealthEdge", result.get(0).getName());
    }

    @Test
    void testInstallApp() {
        UUID appId = UUID.randomUUID();
        App app = new App();
        app.setId(appId);
        app.setTenantId(tenantId);
        app.setTotalInstallations(0);

        when(appRepository.findById(appId)).thenReturn(Optional.of(app));
        when(installationRepository.save(any(AppInstallation.class))).thenAnswer(i -> {
            AppInstallation inst = i.getArgument(0);
            inst.setId(UUID.randomUUID());
            return inst;
        });

        InstallRequest req = new InstallRequest();
        req.setWorkspaceId("ws-123");
        req.setPermissions(List.of("read:user_profile"));

        AppInstallation inst = marketplaceService.installApp(appId, req);

        assertNotNull(inst);
        assertEquals("ws-123", inst.getWorkspaceId());
        assertEquals("TRIAL", inst.getStatus());
        verify(permissionGrantRepository, times(1)).save(any());
        assertEquals(1, app.getTotalInstallations());
    }

    @Test
    void testSubmitAndApproveReview() {
        UUID appId = UUID.randomUUID();
        AppReview review = new AppReview();
        review.setRating(new BigDecimal("4.5"));
        review.setReviewText("Great app!");

        when(reviewRepository.save(any(AppReview.class))).thenReturn(review);

        AppReview savedReview = marketplaceService.submitReview(appId, review);
        assertEquals("PENDING", savedReview.getStatus());

        savedReview.setId(UUID.randomUUID());
        savedReview.setTenantId(tenantId);
        when(reviewRepository.findById(savedReview.getId())).thenReturn(Optional.of(savedReview));
        when(reviewRepository.save(any(AppReview.class))).thenReturn(savedReview);

        AppReview approved = marketplaceService.updateReviewStatus(savedReview.getId(), "APPROVED");
        assertEquals("APPROVED", approved.getStatus());
    }
}
