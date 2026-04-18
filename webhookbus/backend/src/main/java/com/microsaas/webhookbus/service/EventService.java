package com.microsaas.webhookbus.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.webhookbus.entity.DeliveryStatus;
import com.microsaas.webhookbus.entity.WebhookDelivery;
import com.microsaas.webhookbus.entity.WebhookEvent;
import com.microsaas.webhookbus.repository.WebhookDeliveryRepository;
import com.microsaas.webhookbus.repository.WebhookEventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class EventService {

    private final WebhookEventRepository eventRepository;
    private final WebhookDeliveryRepository deliveryRepository;

    public EventService(WebhookEventRepository eventRepository, WebhookDeliveryRepository deliveryRepository) {
        this.eventRepository = eventRepository;
        this.deliveryRepository = deliveryRepository;
    }

    public Page<WebhookEvent> getEvents(Pageable pageable) {
        return eventRepository.findByTenantId(TenantContext.require(), pageable);
    }

    public Page<WebhookDelivery> getDeliveries(UUID eventId, UUID endpointId, DeliveryStatus status, Pageable pageable) {
        UUID tenantId = TenantContext.require();
        if (eventId != null) {
            return deliveryRepository.findByTenantIdAndEventId(tenantId, eventId, pageable);
        } else if (endpointId != null) {
            return deliveryRepository.findByTenantIdAndEndpointId(tenantId, endpointId, pageable);
        } else if (status != null) {
            return deliveryRepository.findByTenantIdAndStatus(tenantId, status, pageable);
        } else {
            return deliveryRepository.findByTenantId(tenantId, pageable);
        }
    }

    public Optional<WebhookDelivery> getDelivery(UUID id) {
        return deliveryRepository.findByIdAndTenantId(id, TenantContext.require());
    }
}
