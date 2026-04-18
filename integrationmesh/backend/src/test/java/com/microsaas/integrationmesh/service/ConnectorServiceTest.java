package com.microsaas.integrationmesh.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.integrationmesh.dto.ConnectorDto;
import com.microsaas.integrationmesh.model.Connector;
import com.microsaas.integrationmesh.repository.ConnectorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConnectorServiceTest {

    @Mock
    private ConnectorRepository connectorRepository;

    @InjectMocks
    private ConnectorService connectorService;

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
    void testCreateConnector() {
        when(connectorRepository.save(any(Connector.class))).thenAnswer(i -> {
            Connector saved = i.getArgument(0);
            saved.setId(UUID.randomUUID());
            return saved;
        });

        ConnectorDto dto = new ConnectorDto();
        dto.setName("Salesforce");
        dto.setType("CRM");

        Connector connector = connectorService.createConnector(dto);

        assertNotNull(connector.getId());
        assertEquals("Salesforce", connector.getName());
        assertEquals("ACTIVE", connector.getStatus());
        assertEquals(tenantId, connector.getTenantId());
    }

    @Test
    void testListConnectors() {
        when(connectorRepository.findByTenantId(tenantId)).thenReturn(List.of(new Connector()));
        assertEquals(1, connectorService.listConnectors().size());
    }
}
