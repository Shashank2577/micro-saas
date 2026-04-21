package com.microsaas.constructioniq.project;

import com.crosscutting.starter.tenancy.TenantContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SiteServiceTest {

    @Mock
    private SiteRepository siteRepository;

    @InjectMocks
    private SiteService siteService;

    private final UUID tenantId = UUID.randomUUID();
    private final UUID projectId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        TenantContext.set(tenantId);
    }

    @Test
    void createSite_SetsProjectTenantAndTimestamps() {
        Site site = new Site();
        site.setName("Test Site");

        when(siteRepository.save(any(Site.class))).thenAnswer(i -> i.getArguments()[0]);

        Site created = siteService.createSite(projectId, site);

        assertEquals(projectId, created.getProjectId());
        assertEquals(tenantId, created.getTenantId());
        assertNotNull(created.getCreatedAt());
        verify(siteRepository).save(site);
    }

    @Test
    void getSite_ThrowsExceptionWhenNotFound() {
        UUID siteId = UUID.randomUUID();
        when(siteRepository.findByIdAndTenantId(siteId, tenantId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> siteService.getSite(siteId));
    }
}
