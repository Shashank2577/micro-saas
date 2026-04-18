package com.microsaas.integrationbridge.service;

import com.microsaas.integrationbridge.model.Integration;
import com.microsaas.integrationbridge.repository.IntegrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class IntegrationService {

    private final IntegrationRepository integrationRepository;
    private final CredentialService credentialService;

    public IntegrationService(IntegrationRepository integrationRepository, CredentialService credentialService) {
        this.integrationRepository = integrationRepository;
        this.credentialService = credentialService;
    }

    @Transactional
    public Integration createIntegration(UUID tenantId, String provider, String authType) {
        Integration integration = new Integration();
        integration.setTenantId(tenantId);
        integration.setProvider(provider);
        integration.setAuthType(authType);
        integration.setStatus("PENDING");
        return integrationRepository.save(integration);
    }

    public List<Integration> getIntegrations(UUID tenantId) {
        return integrationRepository.findByTenantId(tenantId);
    }

    public Optional<Integration> getIntegration(UUID id, UUID tenantId) {
        return integrationRepository.findByIdAndTenantId(id, tenantId);
    }

    @Transactional
    public Integration updateStatus(UUID id, UUID tenantId, String status) {
        Integration integration = integrationRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("Integration not found"));
        integration.setStatus(status);
        return integrationRepository.save(integration);
    }
    
    @Transactional
    public void saveOauthToken(UUID id, UUID tenantId, String code) {
        // Mock token exchange
        String mockToken = "oauth_token_for_" + code;
        credentialService.saveCredentials(id, tenantId, mockToken, "refresh_" + code, null, null);
        updateStatus(id, tenantId, "HEALTHY");
    }
}
