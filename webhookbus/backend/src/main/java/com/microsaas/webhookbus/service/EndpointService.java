package com.microsaas.webhookbus.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.webhookbus.entity.EndpointStatus;
import com.microsaas.webhookbus.entity.WebhookEndpoint;
import com.microsaas.webhookbus.repository.WebhookEndpointRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EndpointService {

    private final WebhookEndpointRepository endpointRepository;

    public EndpointService(WebhookEndpointRepository endpointRepository) {
        this.endpointRepository = endpointRepository;
    }

    public WebhookEndpoint createEndpoint(String name, String url) {
        WebhookEndpoint endpoint = new WebhookEndpoint();
        endpoint.setTenantId(TenantContext.require());
        endpoint.setName(name);
        endpoint.setUrl(url);
        endpoint.setSecret(generateSecret());
        endpoint.setStatus(EndpointStatus.ACTIVE);
        return endpointRepository.save(endpoint);
    }

    public List<WebhookEndpoint> getEndpoints() {
        return endpointRepository.findByTenantId(TenantContext.require());
    }
    
    public List<WebhookEndpoint> getActiveEndpoints() {
        return endpointRepository.findByTenantIdAndStatus(TenantContext.require(), EndpointStatus.ACTIVE);
    }

    public Optional<WebhookEndpoint> getEndpoint(UUID id) {
        return endpointRepository.findByIdAndTenantId(id, TenantContext.require());
    }

    private String generateSecret() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return "whsec_" + Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
