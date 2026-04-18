package com.microsaas.webhookbus.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.webhookbus.entity.DeliveryStatus;
import com.microsaas.webhookbus.entity.WebhookDelivery;
import com.microsaas.webhookbus.entity.WebhookEndpoint;
import com.microsaas.webhookbus.entity.WebhookEvent;
import com.microsaas.webhookbus.repository.WebhookDeliveryRepository;
import com.microsaas.webhookbus.repository.WebhookEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngestionServiceTest {

    @Mock
    private WebhookEventRepository eventRepository;
    
    @Mock
    private WebhookDeliveryRepository deliveryRepository;
    
    @Mock
    private EndpointService endpointService;
    
    @Mock
    private DeliveryService deliveryService;
    
    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private IngestionService ingestionService;

    private final UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        TenantContext.set(tenantId);
    }

    @Test
    void ingest_Success() {
        String source = "github";
        String eventType = "push";
        String payload = "{\"ref\":\"refs/heads/main\"}";

        WebhookEvent event = new WebhookEvent();
        event.setId(UUID.randomUUID());
        when(eventRepository.save(any(WebhookEvent.class))).thenReturn(event);

        WebhookEndpoint endpoint = new WebhookEndpoint();
        endpoint.setId(UUID.randomUUID());
        when(endpointService.getActiveEndpoints()).thenReturn(List.of(endpoint));

        when(deliveryRepository.save(any(WebhookDelivery.class))).thenAnswer(i -> {
            WebhookDelivery d = i.getArgument(0);
            d.setId(UUID.randomUUID());
            return d;
        });

        ingestionService.ingest(source, eventType, payload);

        verify(eventRepository).save(argThat(e -> 
            e.getTenantId().equals(tenantId) &&
            e.getSource().equals(source) &&
            e.getEventType().equals(eventType) &&
            e.getPayload().equals(payload)
        ));

        verify(deliveryRepository).save(argThat(d -> 
            d.getTenantId().equals(tenantId) &&
            d.getEvent() == event &&
            d.getEndpoint() == endpoint &&
            d.getStatus() == DeliveryStatus.PENDING
        ));

        verify(deliveryService).attemptDelivery(any(WebhookDelivery.class));
    }
}
