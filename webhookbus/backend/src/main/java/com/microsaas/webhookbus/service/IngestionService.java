package com.microsaas.webhookbus.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.webhookbus.entity.DeliveryStatus;
import com.microsaas.webhookbus.entity.WebhookDelivery;
import com.microsaas.webhookbus.entity.WebhookEndpoint;
import com.microsaas.webhookbus.entity.WebhookEvent;
import com.microsaas.webhookbus.repository.WebhookDeliveryRepository;
import com.microsaas.webhookbus.repository.WebhookEventRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngestionService {

    private final WebhookEventRepository eventRepository;
    private final WebhookDeliveryRepository deliveryRepository;
    private final EndpointService endpointService;
    private final DeliveryService deliveryService;
    private final ApplicationEventPublisher eventPublisher;

    public IngestionService(WebhookEventRepository eventRepository,
                            WebhookDeliveryRepository deliveryRepository,
                            EndpointService endpointService,
                            DeliveryService deliveryService,
                            ApplicationEventPublisher eventPublisher) {
        this.eventRepository = eventRepository;
        this.deliveryRepository = deliveryRepository;
        this.endpointService = endpointService;
        this.deliveryService = deliveryService;
        this.eventPublisher = eventPublisher;
    }

    public void ingest(String source, String eventType, String payload) {
        WebhookEvent event = new WebhookEvent();
        event.setTenantId(TenantContext.require());
        event.setSource(source != null ? source : "unknown");
        event.setEventType(eventType != null ? eventType : "unknown");
        event.setPayload(payload);
        
        event = eventRepository.save(event);

        List<WebhookEndpoint> endpoints = endpointService.getActiveEndpoints();
        for (WebhookEndpoint endpoint : endpoints) {
            WebhookDelivery delivery = new WebhookDelivery();
            delivery.setTenantId(TenantContext.require());
            delivery.setEvent(event);
            delivery.setEndpoint(endpoint);
            delivery.setStatus(DeliveryStatus.PENDING);
            delivery = deliveryRepository.save(delivery);
            
            deliveryService.attemptDelivery(delivery);
        }
    }
}
