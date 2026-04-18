package com.microsaas.webhookbus.controller;

import com.microsaas.webhookbus.entity.DeliveryStatus;
import com.microsaas.webhookbus.entity.WebhookDelivery;
import com.microsaas.webhookbus.entity.WebhookEvent;
import com.microsaas.webhookbus.service.DeliveryService;
import com.microsaas.webhookbus.service.EventService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class EventController {

    private final EventService eventService;
    private final DeliveryService deliveryService;

    public EventController(EventService eventService, DeliveryService deliveryService) {
        this.eventService = eventService;
        this.deliveryService = deliveryService;
    }

    @GetMapping("/events")
    public ResponseEntity<Page<WebhookEvent>> getEvents(Pageable pageable) {
        return ResponseEntity.ok(eventService.getEvents(pageable));
    }

    @GetMapping("/deliveries")
    public ResponseEntity<Page<WebhookDelivery>> getDeliveries(
            @RequestParam(required = false) UUID eventId,
            @RequestParam(required = false) UUID endpointId,
            @RequestParam(required = false) DeliveryStatus status,
            Pageable pageable) {
        return ResponseEntity.ok(eventService.getDeliveries(eventId, endpointId, status, pageable));
    }

    @PostMapping("/deliveries/{id}/replay")
    public ResponseEntity<Void> replayDelivery(@PathVariable UUID id) {
        Optional<WebhookDelivery> deliveryOpt = eventService.getDelivery(id);
        if (deliveryOpt.isPresent()) {
            WebhookDelivery delivery = deliveryOpt.get();
            delivery.setAttemptCount(0); // Reset attempt count
            delivery.setStatus(DeliveryStatus.PENDING);
            deliveryService.attemptDelivery(delivery);
            return ResponseEntity.accepted().build();
        }
        return ResponseEntity.notFound().build();
    }
}
