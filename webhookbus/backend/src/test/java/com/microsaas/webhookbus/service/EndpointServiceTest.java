package com.microsaas.webhookbus.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.webhookbus.entity.EndpointStatus;
import com.microsaas.webhookbus.entity.WebhookEndpoint;
import com.microsaas.webhookbus.repository.WebhookEndpointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EndpointServiceTest {

    @Mock
    private WebhookEndpointRepository endpointRepository;

    @InjectMocks
    private EndpointService endpointService;

    private final UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        TenantContext.set(tenantId);
    }

    @Test
    void createEndpoint_Success() {
        String name = "Test Endpoint";
        String url = "https://example.com/webhook";
        
        when(endpointRepository.save(any(WebhookEndpoint.class))).thenAnswer(i -> {
            WebhookEndpoint saved = i.getArgument(0);
            saved.setId(UUID.randomUUID());
            return saved;
        });

        WebhookEndpoint endpoint = endpointService.createEndpoint(name, url);

        assertNotNull(endpoint);
        assertEquals(tenantId, endpoint.getTenantId());
        assertEquals(name, endpoint.getName());
        assertEquals(url, endpoint.getUrl());
        assertNotNull(endpoint.getSecret());
        assertEquals(EndpointStatus.ACTIVE, endpoint.getStatus());
        verify(endpointRepository).save(any(WebhookEndpoint.class));
    }

    @Test
    void getEndpoints_ReturnsList() {
        WebhookEndpoint endpoint = new WebhookEndpoint();
        when(endpointRepository.findByTenantId(tenantId)).thenReturn(List.of(endpoint));

        List<WebhookEndpoint> result = endpointService.getEndpoints();

        assertEquals(1, result.size());
        verify(endpointRepository).findByTenantId(tenantId);
    }

    @Test
    void getEndpoint_ReturnsOptional() {
        UUID id = UUID.randomUUID();
        WebhookEndpoint endpoint = new WebhookEndpoint();
        when(endpointRepository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(endpoint));

        Optional<WebhookEndpoint> result = endpointService.getEndpoint(id);

        assertTrue(result.isPresent());
        verify(endpointRepository).findByIdAndTenantId(id, tenantId);
    }
}
