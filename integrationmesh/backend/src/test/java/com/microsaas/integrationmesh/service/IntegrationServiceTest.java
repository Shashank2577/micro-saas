package com.microsaas.integrationmesh.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.integrationmesh.dto.IntegrationDto;
import com.microsaas.integrationmesh.model.Connector;
import com.microsaas.integrationmesh.model.Integration;
import com.microsaas.integrationmesh.repository.ConnectorRepository;
import com.microsaas.integrationmesh.repository.IntegrationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IntegrationServiceTest {

    @Mock
    private IntegrationRepository integrationRepository;

    @Mock
    private ConnectorRepository connectorRepository;

    @InjectMocks
    private IntegrationService integrationService;

    private UUID tenantId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testCreateIntegration() {
        UUID sourceId = UUID.randomUUID();
        UUID targetId = UUID.randomUUID();

        Connector source = new Connector();
        source.setId(sourceId);
        Connector target = new Connector();
        target.setId(targetId);

        when(connectorRepository.findByIdAndTenantId(sourceId, tenantId)).thenReturn(Optional.of(source));
        when(connectorRepository.findByIdAndTenantId(targetId, tenantId)).thenReturn(Optional.of(target));
        when(integrationRepository.save(any(Integration.class))).thenAnswer(i -> {
            Integration saved = i.getArgument(0);
            saved.setId(UUID.randomUUID());
            return saved;
        });

        IntegrationDto dto = new IntegrationDto();
        dto.setName("Test Integration");
        dto.setSourceConnectorId(sourceId);
        dto.setTargetConnectorId(targetId);

        Integration integration = integrationService.createIntegration(dto);

        assertNotNull(integration.getId());
        assertEquals("Test Integration", integration.getName());
        assertEquals("PAUSED", integration.getStatus());
        assertEquals(tenantId, integration.getTenantId());
    }
}
