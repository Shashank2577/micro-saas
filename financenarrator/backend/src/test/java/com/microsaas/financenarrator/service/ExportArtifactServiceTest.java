package com.microsaas.financenarrator.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.financenarrator.model.ExportArtifact;
import com.microsaas.financenarrator.repository.ExportArtifactRepository;
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
public class ExportArtifactServiceTest {

    @Mock
    private ExportArtifactRepository repository;

    @Mock
    private EventPublisherService eventPublisher;

    @InjectMocks
    private ExportArtifactService service;

    private MockedStatic<TenantContext> tenantContextMockedStatic;
    private UUID tenantId;
    private UUID artifactId;
    private ExportArtifact artifact;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        artifactId = UUID.randomUUID();
        tenantContextMockedStatic = Mockito.mockStatic(TenantContext.class);
        tenantContextMockedStatic.when(TenantContext::require).thenReturn(tenantId);

        artifact = new ExportArtifact();
        artifact.setId(artifactId);
        artifact.setTenantId(tenantId);
        artifact.setName("Q3 Financial Report PDF");
        artifact.setStatus("DRAFT");
    }

    @AfterEach
    void tearDown() {
        tenantContextMockedStatic.close();
    }

    @Test
    void testCreateExport_success() {
        when(repository.save(any(ExportArtifact.class))).thenReturn(artifact);

        ExportArtifact result = service.create(artifact);

        assertNotNull(result);
        assertEquals(tenantId, result.getTenantId());
        verify(repository, times(1)).save(artifact);
        verify(eventPublisher, times(1)).publishEvent(
                eq("financenarrator.narrative.generated"),
                eq(tenantId),
                eq("financenarrator"),
                any(Map.class)
        );
    }

    @Test
    void testGetExport_byId() {
        when(repository.findByIdAndTenantId(artifactId, tenantId)).thenReturn(Optional.of(artifact));

        ExportArtifact result = service.getById(artifactId);

        assertNotNull(result);
        assertEquals(artifactId, result.getId());
        verify(repository, times(1)).findByIdAndTenantId(artifactId, tenantId);
    }
}
