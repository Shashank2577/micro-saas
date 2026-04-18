package com.microsaas.integrationbridge;

import com.microsaas.integrationbridge.model.Integration;
import com.microsaas.integrationbridge.repository.IntegrationRepository;
import com.microsaas.integrationbridge.service.CredentialService;
import com.microsaas.integrationbridge.service.IntegrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class IntegrationServiceTest {

    private IntegrationRepository integrationRepository;
    private CredentialService credentialService;
    private IntegrationService integrationService;

    @BeforeEach
    void setUp() {
        integrationRepository = Mockito.mock(IntegrationRepository.class);
        credentialService = Mockito.mock(CredentialService.class);
        integrationService = new IntegrationService(integrationRepository, credentialService);
    }

    @Test
    void testCreateIntegration() {
        UUID tenantId = UUID.randomUUID();
        Integration integration = new Integration();
        integration.setTenantId(tenantId);
        integration.setProvider("STRIPE");
        integration.setStatus("PENDING");
        
        when(integrationRepository.save(any(Integration.class))).thenReturn(integration);
        
        Integration created = integrationService.createIntegration(tenantId, "STRIPE", "OAUTH2");
        assertEquals("STRIPE", created.getProvider());
        assertEquals("PENDING", created.getStatus());
    }
}
